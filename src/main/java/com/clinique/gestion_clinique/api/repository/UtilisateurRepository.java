package com.clinique.gestion_clinique.api.repository;

import com.clinique.gestion_clinique.api.model.Utilisateur;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UtilisateurRepository {

    private final EntityManager entityManager;

    public UtilisateurRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Utilisateur findByEmail(String email) {
        try {
            return entityManager
                    .createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Utilisateur findById(Long id) {
        return entityManager.find(Utilisateur.class, id);
    }
}
