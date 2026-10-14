package com.clinique.gestion_clinique.api.resource;

import com.clinique.gestion_clinique.api.dto.CreateDemandeRequest;
import com.clinique.gestion_clinique.api.dto.ReponseRequest;
import com.clinique.gestion_clinique.api.exception.ApiException;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.security.SecurityUserContext;
import com.clinique.gestion_clinique.api.service.DemandeService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/demandes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DemandeResource {

    private final DemandeService demandeService = new DemandeService();

    @Context
    private ContainerRequestContext requestContext;

    @POST
    @RolesAllowed("GENERALISTE")
    public Response creer(CreateDemandeRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(demandeService.creer(request))
                .build();
    }

    @GET
    @RolesAllowed({ "GENERALISTE", "SPECIALISTE" })
    public Response lister(
            @QueryParam("statut") String statut,
            @QueryParam("consultationId") Long consultationId) {
        Utilisateur utilisateur = utilisateurConnecte();

        if (requestContext.getSecurityContext().isUserInRole("SPECIALISTE")) {
            return Response.ok(demandeService.listerPourSpecialiste(utilisateur, statut)).build();
        }

        if (consultationId == null) {
            throw new ApiException(400, "Le parametre consultationId est obligatoire");
        }
        return Response.ok(demandeService.trouverParConsultation(consultationId)).build();
    }

    @PUT
    @Path("/{id}/reponse")
    @RolesAllowed("SPECIALISTE")
    public Response repondre(@PathParam("id") Long id, ReponseRequest request) {
        return Response.ok(demandeService.repondre(id, utilisateurConnecte(), request)).build();
    }

    private Utilisateur utilisateurConnecte() {
        SecurityContext sc = requestContext.getSecurityContext();
        if (sc instanceof SecurityUserContext userContext) {
            return userContext.getUtilisateur();
        }
        return null;
    }
}

