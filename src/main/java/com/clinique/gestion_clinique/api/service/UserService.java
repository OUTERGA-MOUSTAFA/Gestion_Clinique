package com.clinique.gestion_clinique.api.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.UtilisateurRepository;

public class UserService {

    private final UtilisateurRepository utilisateurRepository;

    public UserService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Optional<Utilisateur> authenticate(String email, String password) {
        return findByUsername(email)
                .filter(utilisateur -> BCrypt.checkpw(password, utilisateur.getMotDePasse()));
    }

    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }
}
