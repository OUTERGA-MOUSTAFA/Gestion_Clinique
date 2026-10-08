package com.clinique.gestion_clinique.api.resource;

import javax.print.attribute.standard.Media;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.resource.dto.DemandeExpertiseRequest;
import com.clinique.gestion_clinique.api.resource.dto.ResponseDemandeRequest;
import com.clinique.gestion_clinique.api.service.DemandeService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/demandes")
public class DemandeExpertiseResource {
    @Context
    private SecurityContext securityContext;
    private final DemandeService demandeService;

    public DemandeExpertiseResource() {

        EntityManager entityManager = JpaUtil.createEntityManager();
        this.demandeService = new DemandeService(entityManager);
    }

    @POST
    @RolesAllowed("GENERALISTE")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response creer(DemandeExpertiseRequest request) {

        try {

            DemandeExpertise demande = demandeService.creationDemandeExpertise(request);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(demande)
                    .build();
        } catch (IllegalArgumentException exception) {
            return Response.status(Response.Status.BAD_REQUEST).entity(exception).build();
        }

    }

    @PUT
    @Path("/{id}/reponse")
    @RolesAllowed("SPECIALISTE")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response repondre(@PathParam("id") Long id, ResponseDemandeRequest request) {

        try {

            String email = securityContext.getUserPrincipal().getName();

            DemandeExpertise demande = demandeService.repondreDemandeExpertise(
                    id,
                    request,
                    email);

            return Response
                    .ok(demande)
                    .build();

        } catch (IllegalArgumentException exception) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(exception.getMessage())
                    .build();

        } catch (SecurityException exception) {

            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(exception.getMessage())
                    .build();
        }
    }
}