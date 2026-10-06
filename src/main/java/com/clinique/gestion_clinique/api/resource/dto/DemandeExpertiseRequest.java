package com.clinique.gestion_clinique.api.resource.dto;

import com.clinique.gestion_clinique.api.model.Priorite;


//DTO pour le POST 
public class DemandeExpertiseRequest {
    
     private Long consultationId;

    private Long specialisteId;

    private String question;

    private Priorite priorite;

    public DemandeExpertiseRequest(){}

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Long getSpecialisteId() {
        return specialisteId;
    }

    public void setSpecialisteId(Long specialisteId) {
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

    

}
