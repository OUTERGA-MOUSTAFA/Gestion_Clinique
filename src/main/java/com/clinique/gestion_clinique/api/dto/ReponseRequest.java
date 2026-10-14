package com.clinique.gestion_clinique.api.dto;

public class ReponseRequest {

    private String avis;
    private String recommandations;

    public String getAvis() {
        return avis;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }
}
