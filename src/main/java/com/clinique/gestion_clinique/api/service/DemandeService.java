package com.clinique.gestion_clinique.api.service;

import java.time.LocalDateTime;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.Specialite;
import com.clinique.gestion_clinique.api.model.StatutDemande;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.DemandeExperticeRepository;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;
import com.clinique.gestion_clinique.api.repository.UtilisateurRepository;
import com.clinique.gestion_clinique.api.resource.dto.DemandeExpertiseRequest;
import com.clinique.gestion_clinique.api.resource.dto.ResponseDemandeRequest;

import jakarta.persistence.EntityManager;

public class DemandeService {
    private final DemandeExperticeRepository demandeRepository;

    private final SpecialisteRepository specialisteRepository;
    private final UtilisateurRepository userRepository;

    public DemandeService(EntityManager entityManager) {
        this.demandeRepository = new DemandeExperticeRepository(entityManager);

        this.specialisteRepository = new SpecialisteRepository(entityManager);

        this.userRepository = new UtilisateurRepository(entityManager);
    }

    // US2 : CRÉER UNE DEMANDE D'EXPERTISE
    public DemandeExpertise repondreDemandeExpertise(
            Long id,
            ResponseDemandeRequest request,
            String email) {

        // 1. Vérifier que la demande existe
        DemandeExpertise demande = demandeRepository.findById(id);

        if (demande == null) {
            throw new IllegalArgumentException(
                    "Demande d'expertise introuvable");
        }

        // 2. Vérifier l'avis
        if (request.getAvis() == null
                || request.getAvis().isBlank()) {

            throw new IllegalArgumentException(
                    "L'avis ne peut pas être vide");
        }

        // 3. Vérifier les recommandations
        if (request.getRecommandations() == null
                || request.getRecommandations().isBlank()) {

            throw new IllegalArgumentException(
                    "Les recommandations ne peuvent pas être vides");
        }

        // 4. Récupérer l'utilisateur connecté
        Utilisateur utilisateur = userRepository.findByEmail(email);

        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "Utilisateur introuvable");
        }

        // 5. Récupérer le spécialiste lié à cet utilisateur
        Specialite specialite = specialisteRepository.findUtilisateurById(
                utilisateur.getId());

        if (specialite == null) {
            throw new IllegalArgumentException(
                    "Spécialiste introuvable");
        }

        // 6. Vérifier que la demande appartient
        // au spécialiste connecté
        if (demande.getSpecialiteId() != specialite.getId()) {

            throw new SecurityException(
                    "Cette demande ne vous est pas destinée");
        }

        // 7. Enregistrer l'avis
        demande.setAvis(request.getAvis());

        // 8. Enregistrer les recommandations
        demande.setRecommandations(
                request.getRecommandations());

        // 9. La demande est maintenant terminée
        demande.setStatut(
                StatutDemande.TERMINEE);

        // 10. Mettre à jour la DB
        return demandeRepository.update(demande);
    }

    public DemandeExpertise creationDemandeExpertise(
            DemandeExpertiseRequest request) {

        if (request.getQuestion() == null
                || request.getQuestion().isBlank()) {

            throw new IllegalArgumentException(
                    "La question ne peut pas être vide");
        }

        if (request.getPriorite() == null) {

            throw new IllegalArgumentException(
                    "La priorité ne peut pas être nulle");
        }

        Specialite specialiste = specialisteRepository.findUtilisateurById(
                request.getSpecialisteId());

        if (specialiste == null) {

            throw new IllegalArgumentException(
                    "Spécialiste introuvable");
        }

        DemandeExpertise demande = new DemandeExpertise();

        demande.setConsultationId(
                request.getConsultationId());

        demande.setSpecialisteId(
                request.getSpecialisteId());

        demande.setQuestion(
                request.getQuestion());

        demande.setPriorite(
                request.getPriorite());

        demande.setStatut(
                StatutDemande.EN_ATTENTE);

        demande.setDate_de_creation(
                LocalDateTime.now());

        return demandeRepository.save(demande);
    }
}
