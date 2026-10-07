package com.clinique.gestion_clinique.api.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *  Classe utilitaire responsable de la création * de l'EntityManager utilisé
 * par JPA.
 */
public class JpaUtil {
    /**
     *  EntityManagerFactory représente la configuration * et les ressources
     * nécessaires pour communiquer * avec la base de données. * * Elle est créée
     * une seule fois.
     */
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("cliniquePU");

    /** * Constructeur privé : * cette classe ne doit pas être instanciée. */
    private JpaUtil() {
    }

    /**
     *  Crée un nouvel EntityManager. * * L'EntityManager sera utilisé par les
     * repositories * pour exécuter les requêtes JPA.  @return un EntityManager
     */
    public static EntityManager createEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}