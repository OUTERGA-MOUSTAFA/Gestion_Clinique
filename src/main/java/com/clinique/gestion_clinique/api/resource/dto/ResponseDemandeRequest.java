package com.clinique.gestion_clinique.api.resource.dto;

public class ResponseDemandeRequest {
    private String avis ;

    private String recommandations;


    public ResponseDemandeRequest(){}

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
