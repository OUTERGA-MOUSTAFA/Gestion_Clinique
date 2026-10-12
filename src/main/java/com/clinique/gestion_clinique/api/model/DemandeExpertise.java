package com.clinique.gestion_clinique.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "demande_expertise")
public class DemandeExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consultation_id", nullable = false)
    private Long consultationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private Specialiste specialiste;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorite priorite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut;

    @Column(columnDefinition = "TEXT")
    private String avis;

    @Column(columnDefinition = "TEXT")
    private String recommandations;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    public DemandeExpertise() {
    }

    public Long getId() {
        return id;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public Specialiste getSpecialiste() {
        return specialiste;
    }

    public String getQuestion() {
        return question;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public String getAvis() {
        return avis;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public void setSpecialiste(Specialiste specialiste) {
        this.specialiste = specialiste;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
