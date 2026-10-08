package com.clinique.gestion_clinique.api.repository;

import java.util.Optional;

import com.clinique.gestion_clinique.api.model.Utilisateur;

public interface UtilisateurRepository {
    Optional<Utilisateur> findByUsername(String username);
}
