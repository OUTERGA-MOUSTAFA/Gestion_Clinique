package com.clinique.gestion_clinique.api.service;

import java.time.LocalDateTime;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.Priorite;
import com.clinique.gestion_clinique.api.model.Specialiste;
import com.clinique.gestion_clinique.api.model.StatutDemande;
import com.clinique.gestion_clinique.api.repository.ConsultationChecker;
import com.clinique.gestion_clinique.api.repository.DemandeExpertiseRepository;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;
import com.clinique.gestion_clinique.api.resource.exception.BadRequestException;
import com.clinique.gestion_clinique.api.resource.exception.NotFoundException;

public class DemandeExpertiseService {

    private final DemandeExpertiseRepository demandeExpertiseRepository;
    private final SpecialisteRepository specialisteRepository;
    private final ConsultationChecker consultationChecker;

    public DemandeExpertiseService(
            DemandeExpertiseRepository demandeExpertiseRepository,
            SpecialisteRepository specialisteRepository,
            ConsultationChecker consultationChecker) {
        this.demandeExpertiseRepository = demandeExpertiseRepository;
        this.specialisteRepository = specialisteRepository;
        this.consultationChecker = consultationChecker;
    }

    public DemandeExpertise createDemande(
            Long consultationId,
            Long specialisteId,
            String question,
            String prioriteStr) {
        if (question == null || question.isBlank()) {
            throw new BadRequestException("La question ne peut pas être vide");
        }
        if (prioriteStr == null) {
            throw new BadRequestException("Priorité invalide");
        }

        Priorite priorite;
        try {
            priorite = Priorite.valueOf(prioriteStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Priorité invalide");
        }

        Specialiste specialiste = specialisteId == null
                ? null
                : specialisteRepository.findById(specialisteId).orElse(null);
        if (specialiste == null) {
            throw new NotFoundException("Spécialiste introuvable");
        }
        if (consultationId == null || !consultationChecker.existsById(consultationId)) {
            throw new NotFoundException("Consultation introuvable");
        }

        DemandeExpertise demande = new DemandeExpertise();
        demande.setConsultationId(consultationId);
        demande.setSpecialiste(specialiste);
        demande.setQuestion(question);
        demande.setPriorite(priorite);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateCreation(LocalDateTime.now());
        return demandeExpertiseRepository.save(demande);
    }
}
