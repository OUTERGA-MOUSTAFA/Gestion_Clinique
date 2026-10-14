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

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        if ("ping".equals(path) || "OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization == null || !authorization.startsWith("Basic ")) {
            abortUnauthorized(requestContext);
            return;
        }

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            String encodedCredentials = authorization.substring("Basic ".length()).trim();
            String credentials = new String(
                    Base64.getDecoder().decode(encodedCredentials),
                    StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) {
                abortUnauthorized(requestContext);
                return;
            }

            Utilisateur utilisateur = new UtilisateurRepository(entityManager).findByEmail(parts[0]);
            if (utilisateur == null
                    || utilisateur.getMotDePasse() == null
                    || !BCrypt.checkpw(parts[1], utilisateur.getMotDePasse())) {
                abortUnauthorized(requestContext);
                return;
            }

            boolean secure = "https".equalsIgnoreCase(
                    requestContext.getUriInfo().getRequestUri().getScheme());
            requestContext.setSecurityContext(new SecurityUserContext(utilisateur, secure));
        } catch (Exception e) {
            abortUnauthorized(requestContext);
        } finally {
            entityManager.close();
        }
    }

    private void abortUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"Clinique API\"")
                        .build());
    }
}
