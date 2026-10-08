package com.clinique.gestion_clinique.api.resource.dto;

public class ReponseRequest {

    private String avis;
    private String recommandations;

    public ReponseRequest() {
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
}
