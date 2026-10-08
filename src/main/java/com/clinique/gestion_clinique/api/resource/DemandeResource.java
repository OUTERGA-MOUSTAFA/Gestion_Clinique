package com.clinique.gestion_clinique.api.resource;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;
import com.clinique.gestion_clinique.api.repository.jpa.JpaConsultationChecker;
import com.clinique.gestion_clinique.api.repository.jpa.JpaDemandeExpertiseRepository;
import com.clinique.gestion_clinique.api.resource.dto.DemandeRequest;
import com.clinique.gestion_clinique.api.service.DemandeExpertiseService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/demandes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DemandeResource {

    private final DemandeExpertiseService demandeExpertiseService;

    public DemandeResource() {
        this.demandeExpertiseService = new DemandeExpertiseService(
                new JpaDemandeExpertiseRepository(),
                new SpecialisteRepository(),
                new JpaConsultationChecker());
    }

    @POST
    public Response create(DemandeRequest request) {
        DemandeExpertise demande = demandeExpertiseService.createDemande(
                request.getConsultationId(),
                request.getSpecialisteId(),
                request.getQuestion(),
                request.getPriorite());
        return Response.status(Response.Status.CREATED)
                .entity(demande)
                .build();
    }
}
