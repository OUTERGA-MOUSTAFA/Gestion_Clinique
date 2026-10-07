package com.clinique.gestion_clinique.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    /**
     * Identifiant unique de l'utilisateur.
     *
     * GenerationType.IDENTITY permet à MySQL
     * de générer automatiquement l'id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de famille de l'utilisateur.
     */
    private String nom;

    /**
     * Prénom de l'utilisateur.
     */
    private String prenom;

    /**
     * Email utilisé pour l'authentification.
     */
    private String email;

    /**
     * Mot de passe stocké sous forme de hash BCrypt.
     */
    private String mot_de_passe;

    /**
     * Rôle de l'utilisateur.
     *
     * EnumType.STRING permet de stocker :
     *
     * GENERALISTE
     * SPECIALISTE
     *
     * au lieu de stocker 0, 1, etc.
     */
    @Enumerated(EnumType.STRING)
    private Role role;


    /**
     * Constructeur vide obligatoire pour JPA.
     */
    public Utilisateur() {
    }


    // =========================
    // Getters
    // =========================

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

    public String getMotDePasse() {
        return mot_de_passe;
    }

    public Role getRole() {
        return role;
    }


    // =========================
    // Setters
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.mot_de_passe = motDePasse;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
