package com.clinique.gestion_clinique.api.repository;

import com.clinique.gestion_clinique.api.model.Specialiste;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class SpecialisteRepository {

    private final EntityManager entityManager;

    public SpecialisteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
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
}
