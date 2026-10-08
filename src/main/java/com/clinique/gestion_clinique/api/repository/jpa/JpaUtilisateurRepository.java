package com.clinique.gestion_clinique.api.repository.jpa;

import java.util.Optional;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.UtilisateurRepository;

import jakarta.persistence.EntityManager;

public class JpaUtilisateurRepository implements UtilisateurRepository {

    @Override
    public Optional<Utilisateur> findByUsername(String username) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :u",
                    Utilisateur.class)
                    .setParameter("u", username)
                    .getResultStream()
                    .findFirst();
        } finally {
            entityManager.close();
        }
    }
}
