package com.clinique.gestion_clinique.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "demande_expertise")
public class DemandeExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long consultationId;
    private long specialisteId;
    private String question;
    @Enumerated(EnumType.STRING)
    private Priorite priorite;
    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    private String avis;
    private String recommandations;
    private LocalDateTime date_de_creation;

    public DemandeExpertise() {
    }

    public long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(long consultationId) {
        this.consultationId = consultationId;
    }

    public long getSpecialiteId() {
        return specialisteId;
    }

    public void setSpecialisteId(long specialisteId) {
        this.specialisteId = specialisteId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public LocalDateTime getDate_de_creation() {
        return date_de_creation;
    }

    public void setDate_de_creation(LocalDateTime date_de_creation) {
        this.date_de_creation = date_de_creation;
    }

}
