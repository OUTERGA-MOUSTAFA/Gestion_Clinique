package com.clinique.gestion_clinique.api.dto;

public class CreateDemandeRequest {

    private Long consultationId;
    private Long specialisteId;
    private String question;
    private String priorite;

    public Long getConsultationId() {
        return consultationId;
    }

    public Long getSpecialisteId() {
        return specialisteId;
    }

    public String getQuestion() {
        return question;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public void setSpecialisteId(Long specialisteId) {
        this.specialisteId = specialisteId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }
}
