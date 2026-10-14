package com.clinique.gestion_clinique.api.repository;

import java.util.List;

import com.clinique.gestion_clinique.api.model.Specialiste;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class SpecialisteRepository {

    private final EntityManager entityManager;

    public SpecialisteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Specialiste> findAll() {
        return entityManager
                .createQuery(
                        "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur",
                        Specialiste.class)
                .getResultList();
    }

    public Specialiste findById(Long id) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur WHERE s.id = :id",
                            Specialiste.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Specialiste findByUtilisateurId(Long utilisateurId) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur"
                                    + " WHERE s.utilisateur.id = :utilisateurId",
                            Specialiste.class)
                    .setParameter("utilisateurId", utilisateurId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
