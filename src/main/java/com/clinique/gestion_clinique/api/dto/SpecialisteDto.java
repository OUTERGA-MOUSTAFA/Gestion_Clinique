package com.clinique.gestion_clinique.api.dto;

import com.clinique.gestion_clinique.api.model.Specialiste;
import com.clinique.gestion_clinique.api.model.Specialite;

public class SpecialisteDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Specialite specialite;
    private double tarif;

    public static SpecialisteDto from(Specialiste specialiste) {
        SpecialisteDto dto = new SpecialisteDto();
        dto.id = specialiste.getId();
        dto.specialite = specialiste.getSpecialite();
        dto.tarif = specialiste.getTarif();
        if (specialiste.getUtilisateur() != null) {
            dto.nom = specialiste.getUtilisateur().getNom();
            dto.prenom = specialiste.getUtilisateur().getPrenom();
            dto.email = specialiste.getUtilisateur().getEmail();
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public double getTarif() {
        return tarif;
    }
}
