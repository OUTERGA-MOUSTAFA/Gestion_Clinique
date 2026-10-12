package com.clinique.gestion_clinique.api.repository;

import java.util.List;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class DemandeRepository {

    private final EntityManager entityManager;

    public DemandeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public DemandeExpertise save(DemandeExpertise demande) {
        entityManager.persist(demande);
        return demande;
    }

    public DemandeExpertise update(DemandeExpertise demande) {
        return entityManager.merge(demande);
    }

    public DemandeExpertise findById(Long id) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT d FROM DemandeExpertise d"
                                    + " JOIN FETCH d.specialiste s"
                                    + " JOIN FETCH s.utilisateur"
                                    + " WHERE d.id = :id",
                            DemandeExpertise.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<DemandeExpertise> findBySpecialisteId(Long specialisteId) {
        return entityManager
                .createQuery(
                        "SELECT d FROM DemandeExpertise d"
                                + " JOIN FETCH d.specialiste s"
                                + " JOIN FETCH s.utilisateur"
                                + " WHERE s.id = :specialisteId",
                        DemandeExpertise.class)
                .setParameter("specialisteId", specialisteId)
                .getResultList();
    }

    public DemandeExpertise findByConsultationId(Long consultationId) {
        List<DemandeExpertise> demandes = entityManager
                .createQuery(
                        "SELECT d FROM DemandeExpertise d"
                                + " JOIN FETCH d.specialiste s"
                                + " JOIN FETCH s.utilisateur"
                                + " WHERE d.consultationId = :consultationId"
                                + " ORDER BY d.dateCreation DESC",
                        DemandeExpertise.class)
                .setParameter("consultationId", consultationId)
                .getResultList();
        return demandes.isEmpty() ? null : demandes.get(0);
    }
}
