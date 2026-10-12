package com.clinique.gestion_clinique.api.dto;

import java.time.LocalDateTime;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;
import com.clinique.gestion_clinique.api.model.Priorite;
import com.clinique.gestion_clinique.api.model.StatutDemande;

public class DemandeDto {

    private Long id;
    private Long consultationId;
    private Long specialisteId;
    private String question;
    private Priorite priorite;
    private StatutDemande statut;
    private String avis;
    private String recommandations;
    private LocalDateTime dateCreation;
    private SpecialisteDto specialiste;

    public static DemandeDto from(DemandeExpertise demande) {
        DemandeDto dto = new DemandeDto();
        dto.id = demande.getId();
        dto.consultationId = demande.getConsultationId();
        dto.question = demande.getQuestion();
        dto.priorite = demande.getPriorite();
        dto.statut = demande.getStatut();
        dto.avis = demande.getAvis();
        dto.recommandations = demande.getRecommandations();
        dto.dateCreation = demande.getDateCreation();
        if (demande.getSpecialiste() != null) {
            dto.specialisteId = demande.getSpecialiste().getId();
            dto.specialiste = SpecialisteDto.from(demande.getSpecialiste());
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public Long getSpecialisteId() {
        return specialisteId;
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

    public SpecialisteDto getSpecialiste() {
        return specialiste;
    }
}
