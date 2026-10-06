package com.clinique.gestion_clinique.api.repository.jpa;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.repository.ConsultationChecker;

import jakarta.persistence.EntityManager;

public class JpaConsultationChecker implements ConsultationChecker {

    @Override
    public boolean existsById(Long id) {
        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            Long count = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Consultation c WHERE c.id = :id",
                    Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            entityManager.close();
        }
    }
}
