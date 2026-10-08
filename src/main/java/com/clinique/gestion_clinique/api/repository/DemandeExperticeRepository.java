package com.clinique.gestion_clinique.api.repository;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;

import jakarta.persistence.EntityManager;

public class DemandeExperticeRepository {
    

    private final EntityManager entityManager;

    public DemandeExperticeRepository(EntityManager entityManager){

        this.entityManager = entityManager;
    }


public DemandeExpertise findById(Long id){
        return entityManager.find(DemandeExpertise.class, id);
    }

    public DemandeExpertise save(DemandeExpertise demande){

        entityManager.getTransaction().begin();
        entityManager.persist(demande);//INSERT INTO demande_expertise
        entityManager.getTransaction().commit();
        return demande;
    }

    public DemandeExpertise update(DemandeExpertise demande){

        entityManager.getTransaction().begin();
        entityManager.merge(demande);//UPDATE demande_expertise
        entityManager.getTransaction().commit();
        return demande;
    }

    
}