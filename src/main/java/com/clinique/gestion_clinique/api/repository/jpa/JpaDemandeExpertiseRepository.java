package com.clinique.gestion_clinique.api.repository.jpa;

import java.util.List;
import java.util.Optional;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.repository.DemandeExpertiseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class JpaDemandeExpertiseRepository implements DemandeExpertiseRepository {

    @Override
    public DemandeExpertise save(DemandeExpertise demande) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            DemandeExpertise saved;
            if (demande.getId() == null) {
                entityManager.persist(demande);
                saved = demande;
            } else {
                saved = entityManager.merge(demande);
            }
            entityManager.getTransaction().commit();
            return saved;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<DemandeExpertise> findById(Long id) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(entityManager.find(DemandeExpertise.class, id));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<DemandeExpertise> findBySpecialisteId(Long specialisteId) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT d FROM DemandeExpertise d WHERE d.specialiste.id = :sid",
                    DemandeExpertise.class)
                    .setParameter("sid", specialisteId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<DemandeExpertise> findByConsultationId(Long consultationId) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return Optional.of(entityManager.createQuery(
                    "SELECT d FROM DemandeExpertise d WHERE d.consultationId = :cid",
                    DemandeExpertise.class)
                    .setParameter("cid", consultationId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }
}
