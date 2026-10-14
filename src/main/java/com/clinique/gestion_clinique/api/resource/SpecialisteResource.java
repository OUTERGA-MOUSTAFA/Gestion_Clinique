package com.clinique.gestion_clinique.api.resource;

import com.clinique.gestion_clinique.api.service.SpecialisteService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/specialistes")
@Produces(MediaType.APPLICATION_JSON)
public class SpecialisteResource {

    private final SpecialisteService specialisteService = new SpecialisteService();

    @GET
    @RolesAllowed("GENERALISTE")
    public Response lister(@QueryParam("specialite") String specialite) {
        return Response.ok(specialisteService.listerParSpecialite(specialite)).build();
    }
}
