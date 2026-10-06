package com.clinique.gestion_clinique.api.service;

import java.time.LocalDateTime;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.StatutDemande;
import com.clinique.gestion_clinique.api.repository.DemandeExperticeRepository;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;
import com.clinique.gestion_clinique.api.resource.dto.DemandeExpertiseRequest;

import jakarta.persistence.EntityManager;

public class DemandeService {
    private final DemandeExperticeRepository demandeRepository;

    // private final SpecialisteRepository specialisteRepository;

    public DemandeService(EntityManager entityManager) {
        this.demandeRepository = new DemandeExperticeRepository(entityManager);

        // this.specialisteRepository = new SpecialisteRepository(entityManager);
    }

    public DemandeExpertise creationDemandeExpertise(DemandeExpertiseRequest request) {
        // 1. Vérifier la question
        if (request.getQuestion().isBlank() || request.getQuestion() == null) {
            throw new IllegalArgumentException("La question ne peut pas être vide");
        }
        // 2. Vérifier la priorité
        if (request.getPriorite() == null) {
            throw new IllegalArgumentException("La priorité ne peut pas être nulle");
        }

        // 3 Vérifier la specialisté
        // 3. Vérifier le spécialiste
        // var specialiste = specialisteRepository.findById(
        //         request.getSpecialisteId());

        // if (specialiste == null) {

        //     throw new IllegalArgumentException(
        //             "Spécialiste introuvable");
        // }

        // 4. Créer la demande
        DemandeExpertise demande = new DemandeExpertise();

        demande.setConsultationId(   request.getConsultationId());

        demande.setSpecialisteId(    request.getSpecialisteId());

        demande.setQuestion(         request.getQuestion());

        demande.setPriorite(         request.getPriorite());

        demande.setStatut(           StatutDemande.EN_ATTENTE);

        demande.setDate_de_creation(     LocalDateTime.now());

        return demandeRepository.save(demande);
    }
}
