package com.clinique.gestion_clinique.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entité JPA représentant un médecin spécialiste.
 *
 * Un spécialiste est lié à un compte utilisateur (rôle SPECIALISTE)
 * et possède une spécialité médicale et un tarif de consultation.
 *
 * Table : specialiste
 */
@Entity
@Table(name = "specialiste")
public class Specialiste {

    /** Identifiant auto-généré. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Compte utilisateur associé à ce spécialiste.
     * Relation one-to-one vers la table utilisateur.
     */
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    /**
     * Domaine de spécialité médicale.
     * Stocké en base sous forme de chaîne (ex : "CARDIOLOGIE").
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialite specialite;

    /**
     * Tarif de consultation en dirhams (DH).
     */
    @Column(nullable = false)
    private double tarif;


    /** Constructeur vide obligatoire pour JPA. */
    public Specialiste() {
    }


    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public double getTarif() {
        return tarif;
    }


    // =========================
    // Setters
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }
}
