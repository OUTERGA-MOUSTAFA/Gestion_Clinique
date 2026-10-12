package com.clinique.gestion_clinique.api.repository;

import com.clinique.gestion_clinique.api.model.Consultation;

import jakarta.persistence.EntityManager;

public class ConsultationRepository {

    private final EntityManager entityManager;

    public ConsultationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Consultation findById(Long id) {
        return entityManager.find(Consultation.class, id);
    }
}
