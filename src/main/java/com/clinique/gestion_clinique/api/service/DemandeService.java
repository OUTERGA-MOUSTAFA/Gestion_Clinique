package com.clinique.gestion_clinique.api.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.dto.CreateDemandeRequest;
import com.clinique.gestion_clinique.api.dto.DemandeDto;
import com.clinique.gestion_clinique.api.dto.ReponseRequest;
import com.clinique.gestion_clinique.api.exception.ApiException;
import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.Priorite;
import com.clinique.gestion_clinique.api.model.Specialiste;
import com.clinique.gestion_clinique.api.model.StatutDemande;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.ConsultationRepository;
import com.clinique.gestion_clinique.api.repository.DemandeRepository;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;

import jakarta.persistence.EntityManager;

public class DemandeService {

    public DemandeDto creer(CreateDemandeRequest request) {
        if (request == null
                || request.getQuestion() == null
                || request.getQuestion().isBlank()) {
            throw new ApiException(400, "La question ne peut pas etre vide");
        }
        if (request.getConsultationId() == null || request.getSpecialisteId() == null) {
            throw new ApiException(400, "consultationId et specialisteId sont obligatoires");
        }

        Priorite priorite = parsePriorite(request.getPriorite());

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            ConsultationRepository consultationRepository = new ConsultationRepository(entityManager);
            SpecialisteRepository specialisteRepository = new SpecialisteRepository(entityManager);
            DemandeRepository demandeRepository = new DemandeRepository(entityManager);

            if (consultationRepository.findById(request.getConsultationId()) == null) {
                entityManager.getTransaction().rollback();
                throw new ApiException(404, "Consultation introuvable");
            }

            Specialiste specialiste = specialisteRepository.findById(request.getSpecialisteId());
            if (specialiste == null) {
                entityManager.getTransaction().rollback();
                throw new ApiException(404, "Specialiste introuvable");
            }

            DemandeExpertise demande = new DemandeExpertise();
            demande.setConsultationId(request.getConsultationId());
            demande.setSpecialiste(specialiste);
            demande.setQuestion(request.getQuestion().trim());
            demande.setPriorite(priorite);
            demande.setStatut(StatutDemande.EN_ATTENTE);
            demande.setDateCreation(LocalDateTime.now());

            demandeRepository.save(demande);
            entityManager.getTransaction().commit();
            return DemandeDto.from(demande);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public List<DemandeDto> listerPourSpecialiste(Utilisateur utilisateur, String statutParam) {
        Specialiste specialiste = specialisteConnecte(utilisateur);
        StatutDemande statut = parseStatutOptionnel(statutParam);

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            List<DemandeExpertise> demandes = new DemandeRepository(entityManager)
                    .findBySpecialisteId(specialiste.getId());

            return demandes.stream()
                    .filter(demande -> statut == null || demande.getStatut() == statut)
                    .sorted(Comparator.comparing(DemandeExpertise::getPriorite))
                    .map(DemandeDto::from)
                    .toList();
        } finally {
            entityManager.close();
        }
    }

    public DemandeDto trouverParConsultation(Long consultationId) {
        if (consultationId == null) {
            throw new ApiException(400, "Le parametre consultationId est obligatoire");
        }

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            DemandeExpertise demande = new DemandeRepository(entityManager)
                    .findByConsultationId(consultationId);
            if (demande == null) {
                throw new ApiException(404, "Aucune demande pour cette consultation");
            }
            return DemandeDto.from(demande);
        } finally {
            entityManager.close();
        }
    }

    public DemandeDto repondre(Long demandeId, Utilisateur utilisateur, ReponseRequest request) {
        if (request == null
                || request.getAvis() == null
                || request.getAvis().isBlank()
                || request.getRecommandations() == null
                || request.getRecommandations().isBlank()) {
            throw new ApiException(400, "L'avis et les recommandations sont obligatoires");
        }

        Specialiste specialiste = specialisteConnecte(utilisateur);

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            DemandeRepository demandeRepository = new DemandeRepository(entityManager);
            DemandeExpertise demande = demandeRepository.findById(demandeId);
            if (demande == null) {
                entityManager.getTransaction().rollback();
                throw new ApiException(404, "Demande introuvable");
            }

            if (!demande.getSpecialiste().getId().equals(specialiste.getId())) {
                entityManager.getTransaction().rollback();
                throw new ApiException(403, "Cette demande est adressee a un autre specialiste");
            }

            demande.setAvis(request.getAvis().trim());
            demande.setRecommandations(request.getRecommandations().trim());
            demande.setStatut(StatutDemande.TERMINEE);
            demandeRepository.update(demande);

            entityManager.getTransaction().commit();
            return DemandeDto.from(demande);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    private Specialiste specialisteConnecte(Utilisateur utilisateur) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            Specialiste specialiste = new SpecialisteRepository(entityManager)
                    .findByUtilisateurId(utilisateur.getId());
            if (specialiste == null) {
                throw new ApiException(403, "Aucun profil specialiste pour cet utilisateur");
            }
            return specialiste;
        } finally {
            entityManager.close();
        }
    }

    private Priorite parsePriorite(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new ApiException(400, "La priorite est obligatoire");
        }
        try {
            return Priorite.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "La priorite n'est pas valide");
        }
    }

    private StatutDemande parseStatutOptionnel(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }
        try {
            return StatutDemande.valueOf(valeur.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Le statut n'est pas valide");
        }
    }
}
