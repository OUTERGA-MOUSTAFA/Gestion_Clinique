package com.clinique.gestion_clinique.api.repository;

import java.util.List;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.StatutDemande;

import jakarta.persistence.EntityManager;

public class DemandeExperticeRepository {

    private final EntityManager entityManager;

    public DemandeExperticeRepository(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    public DemandeExpertise findById(Long id) {
        return entityManager.find(DemandeExpertise.class, id);
    }

    public DemandeExpertise save(DemandeExpertise demande) {

        entityManager.getTransaction().begin();
        entityManager.persist(demande);// INSERT INTO demande_expertise
        entityManager.getTransaction().commit();
        return demande;
    }

    public DemandeExpertise update(DemandeExpertise demande) {

        entityManager.getTransaction().begin();
        entityManager.merge(demande);// UPDATE demande_expertise
        entityManager.getTransaction().commit();
        return demande;
    }

    // toutes les demande d'expirtise
    public List<DemandeExpertise> findAll(){
        return entityManager.createQuery("SELECT d FROM DemandeExpertise d", DemandeExpertise.class).getResultList();
    }

    //les demande par status
    public List<DemandeExpertise> findByStatut(StatutDemande status){
        return entityManager.createQuery("SELECT d FROM DemandeExpertise d WHERE d.statut = :status", DemandeExpertise.class)
                .setParameter("status", status)
                .getResultList();
    }

    // les demandes par consultation
    public  List<DemandeExpertise> findByConsultation(Long consultationId){
        return entityManager.createQuery("SELECT d FROM DemandeExpertise d WHERE d.consultationId = :consultationId", DemandeExpertise.class)
                .setParameter("consultationId", consultationId)
                .getResultList();
    }
}