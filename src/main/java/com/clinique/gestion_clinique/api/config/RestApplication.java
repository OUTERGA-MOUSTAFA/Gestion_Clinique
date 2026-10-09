package com.clinique.gestion_clinique.api.config;

import com.clinique.gestion_clinique.api.security.BasicAuthFilter;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import jakarta.ws.rs.ApplicationPath;

/**
 * Configuration principale de l'API REST.
 *
 * Toutes les routes REST commenceront par :/api
 *
 * Exemple :
 *
 * /api/me
 */
// @ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    /**
     * Configuration de Jersey.
     */
    public RestApplication() {

        /**
         Jersey cherche automatiquement les Resources dans ce package.
         Il trouvera par exemple : AuthResource SpecialisteResource etc.
         */
        packages(
                "com.clinique.gestion_clinique.api.resource");

        /**
         Enregistrer le filtre Basic Authentication.
         Ce filtre sera exécuté avant les Resources.
         */
        register(BasicAuthFilter.class);

        /**
          Activer le support de @RolesAllowed.
         Exemple :
          @RolesAllowed("GENERALISTE")
          Si le rôle est incorrect :
          403 Forbidden
         */
        register(RolesAllowedDynamicFeature.class);
    }
}