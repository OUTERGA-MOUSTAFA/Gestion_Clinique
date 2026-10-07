package com.clinique.gestion_clinique.api.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.UtilisateurRepository;

import jakarta.annotation.Priority;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Filtre responsable de l'authentification HTTP Basic.
 *
 * Ce filtre est exécuté avant les ressources REST.
 *
 * Workflow :
 *
 * Client
 * ↓
 * Authorization: Basic email:password
 * ↓
 * BasicAuthFilter
 * ↓
 * Repository
 * ↓
 * Database
 * ↓
 * BCrypt
 * ↓
 * SecurityContext
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    /**
     * EntityManager utilisé pour accéder à la base de données.
     */
    private final EntityManager entityManager = JpaUtil.createEntityManager();

    /**
     * Repository utilisé pour rechercher l'utilisateur.
     */
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository(entityManager);

    /**
     * Méthode appelée automatiquement par Jersey
     * avant l'exécution d'une ressource REST.
     *
    //  ***@param requestContext contexte de la requête HTTP
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {

        /**
         * 1. Récupérer le header Authorization.
         *
         * Exemple :
         *
         * Authorization: Basic bWV...==
         */
        String authorization = requestContext.getHeaderString("Authorization");

        /**
         * 2. Vérifier que le header existe
         * et qu'il utilise l'authentification Basic.
         */
        if (authorization == null ||
                !authorization.startsWith("Basic ")) {

            abortUnauthorized(requestContext);
            return;
        }

        try {

            /**
             * 3. Récupérer uniquement la partie Base64.
             *
             * "Basic abc123"
             * ↓
             * "abc123"
             */
            String encodedCredentials = authorization.substring("Basic ".length());

            /**
             * 4. Décoder Base64.
             *
             * Après décodage, on obtient :
             *
             * email:password
             */
            String credentials = new String(
                    Base64.getDecoder().decode(encodedCredentials),
                    StandardCharsets.UTF_8);

            /**
             * 5. Séparer l'email et le mot de passe.
             *
             * Exemple :
             *
             * medecin@clinique.com:password
             *
             * devient :
             *
             * email = medecin@clinique.com
             * password = password
             */
            String[] parts = credentials.split(":", 2);

            /**
             * Si le format est incorrect,
             * l'utilisateur n'est pas authentifié.
             */
            if (parts.length != 2) {

                abortUnauthorized(requestContext);
                return;
            }

            String email = parts[0];
            String password = parts[1];

            /**
             * 6. Rechercher l'utilisateur dans la base de données
             * à partir de son email.
             */
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

            /**
             * Aucun utilisateur trouvé.
             */
            if (utilisateur == null) {

                abortUnauthorized(requestContext);
                return;
            }

            /**
             * 7. Vérifier le mot de passe.
             *
             * password :
             * mot de passe envoyé par le client
             *
             * utilisateur.getMotDePasse() :
             * hash BCrypt enregistré dans la DB
             *
             * BCrypt compare les deux valeurs.
             */
            boolean passwordCorrect = BCrypt.checkpw(
                    password,
                    utilisateur.getMotDePasse());

            /*
             * Mot de passe incorrect.
             */
            if (!passwordCorrect) {

                abortUnauthorized(requestContext);
                return;
            }

            /**
             * 8. Authentification réussie.
             *
             * On place maintenant l'identité de l'utilisateur
             * dans le SecurityContext.
             */
            SecurityUserContext securityContext = new SecurityUserContext(
                    utilisateur,
                    requestContext.getUriInfo()
                            .getRequestUri()
                            .getScheme()
                            .equalsIgnoreCase("https"));

            /**
             * 9. Injecter notre SecurityContext dans
             * le contexte de la requête.
             *
             * Les Resources pourront ensuite récupérer
             * l'utilisateur connecté.
             */
            requestContext.setSecurityContext(securityContext);

        } catch (Exception e) {

            /**
             * Toute erreur pendant l'authentification
             * est considérée comme une authentification invalide.
             */
            abortUnauthorized(requestContext);
        }
    }

    /**
     * Retourne une réponse HTTP 401 Unauthorized.
     *
     * Le header WWW-Authenticate indique au client
     * que l'API utilise HTTP Basic Authentication.
     */
    private void abortUnauthorized(
            ContainerRequestContext requestContext) {

        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(
                                "WWW-Authenticate",
                                "Basic realm=\"Clinique API\"")
                        .build());
    }
}
