package com.clinique.gestion_clinique.api.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Resource utilisée pour tester l'authentification.
 *
 * Endpoint :
 *
 * GET /api/me
 *
 * Cette Resource ne cherche pas l'utilisateur dans la DB.
 *
 * L'utilisateur a déjà été authentifié
 * par BasicAuthFilter.
 *
 * Elle récupère simplement son identité
 * depuis le SecurityContext.
 */
@Path("/me")
public class AuthResource {

    /**
     * SecurityContext fourni automatiquement par Jersey.
     */
    @Context
    private SecurityContext securityContext;


    /**
     * Retourne l'utilisateur actuellement authentifié.
     *
     * Les deux rôles sont autorisés :
     *
     * GENERALISTE
     * SPECIALISTE
     */
    @GET
    @RolesAllowed({"GENERALISTE", "SPECIALISTE"})
    public Response me() {

        /**
         * Récupérer l'identité de l'utilisateur
         * depuis le SecurityContext.
         */
        String email =
                securityContext
                        .getUserPrincipal()
                        .getName();


        /**
         * Retourner une réponse HTTP 200.
         */
        return Response.ok(
                "Authenticated user: " + email
        ).build();
    }
}