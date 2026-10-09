package com.clinique.gestion_clinique.api.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path 
("/specialistes")
public class SpecialisteResource {
    


    @GET
    public Response testerSpecialistes() {
        return Response.ok("Endpoint specialistes fonctionne").build();
    }
}
