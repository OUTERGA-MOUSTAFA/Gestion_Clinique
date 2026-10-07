package com.clinique.gestion_clinique.api.security;

import java.security.Principal;

import com.clinique.gestion_clinique.api.model.Utilisateur;

import jakarta.ws.rs.core.SecurityContext;

/**
 * SecurityContext personnalisé de l'application.
 *
 * Il contient l'utilisateur authentifié
 * et permet à Jersey de connaître son identité
 * et son rôle.
 */
public class SecurityUserContext implements SecurityContext {

    /**
     * Utilisateur actuellement authentifié.
     */
    private final Utilisateur utilisateur;


    /**
     * Indique si la requête utilise HTTPS.
     */
    private final boolean secure;


    /**
     * Constructeur.
     *
     * @param utilisateur utilisateur authentifié
     * @param secure indique si la connexion est sécurisée
     */
    public SecurityUserContext(
            Utilisateur utilisateur,
            boolean secure) {

        this.utilisateur = utilisateur;
        this.secure = secure;
    }


    /**
     * Retourne l'identité de l'utilisateur connecté.
     *
     * Jersey utilisera cette méthode lorsqu'une Resource
     * demande :
     *
     * securityContext.getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {

        return () -> utilisateur.getEmail();
    }


    /**
     * Vérifie si l'utilisateur possède un rôle donné.
     *
     * Exemple :
     *
     * @RolesAllowed("GENERALISTE")
     *
     * Jersey appellera cette méthode avec :
     *
     * isUserInRole("GENERALISTE")
     */
    @Override
    public boolean isUserInRole(String role) {

        return utilisateur.getRole() != null
                && utilisateur.getRole().name().equals(role);
    }


    /**
     * Indique si la connexion utilise HTTPS.
     */
    @Override
    public boolean isSecure() {

        return secure;
    }


    /**
     * Retourne le type d'authentification utilisé.
     */
    @Override
    public String getAuthenticationScheme() {

        return SecurityContext.BASIC_AUTH;
    }


    /**
     * Permet de récupérer l'objet Utilisateur complet
     * lorsque l'application en a besoin.
     */
    public Utilisateur getUtilisateur() {

        return utilisateur;
    }
}