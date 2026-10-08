package com.clinique.gestion_clinique.api.repository;

import java.util.Optional;

import com.clinique.gestion_clinique.api.model.Specialiste;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class SpecialisteRepository {

    private final EntityManager entityManager;

    public SpecialisteRepository() {
        this.entityManager = null;
    }

    public SpecialisteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Specialiste> findById(Long id) {
        EntityManager manager = entityManager == null ? JpaUtil.createEntityManager() : entityManager;
        try {
            return Optional.ofNullable(manager.find(Specialiste.class, id));
        } finally {
            if (entityManager == null) {
                manager.close();
            }
        }
    }

    public Specialiste findUtilisateurById(Long utilisateurId) {

        try {

            return entityManager.createQuery(
                    "SELECT s FROM Specialiste s " +
                            "WHERE s.utilisateurId = :utilisateurId",
                    Specialiste.class)
                    .setParameter("utilisateurId", utilisateurId)
                    .getSingleResult();

        } catch (NoResultException e) {

            return null;
        }
    }

    public Optional<Specialiste> findByUtilisateurId(Long utilisateurId) {
        return entityManager.createQuery(
                "SELECT s FROM Specialiste s WHERE s.utilisateurId = :uid",
                Specialiste.class)
                .setParameter("uid", utilisateurId)
                .getResultStream()
                .findFirst();
    }
}
