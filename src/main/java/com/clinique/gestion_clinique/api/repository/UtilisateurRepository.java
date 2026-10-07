package com.clinique.gestion_clinique.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import com.clinique.gestion_clinique.api.model.Utilisateur;

/**
 * Repository responsable de l'accès aux données * de l'entité Utilisateur. *
 * Le Repository communique avec la base de données * à travers
 * l'EntityManager.
 */
public class UtilisateurRepository {
    /** * EntityManager utilisé pour exécuter * les requêtes JPA. */
    private final EntityManager entityManager;

    /**
     * Constructeur du Repository. * * @param entityManager EntityManager utilisé
     * pour la DB
     */
    public UtilisateurRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Recherche un utilisateur à partir de son email. * * Cette méthode sera
     * utilisée plus tard * par le BasicAuthFilter. * * Workflow : * *
     * BasicAuthFilter * ↓ * Repository * ↓ * EntityManager * ↓ * MySQL * * @param
     * email email de l'utilisateur * @return l'utilisateur trouvé ou null
     */
    public Utilisateur findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            /* * Aucun utilisateur ne possède cet email. */ return null;
        }
    }

    /**
     * Recherche un utilisateur par son identifiant. * * @param id identifiant de
     * l'utilisateur * @return l'utilisateur trouvé ou null
     */
    public Utilisateur findById(Long id) {
        try {
            return entityManager.createQuery("SELECT u FROM Utilisateur u WHERE u.id = :id", Utilisateur.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}