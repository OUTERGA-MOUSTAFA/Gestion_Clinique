package com.clinique.gestion_clinique.api.repository;

import java.util.List;
import java.util.Optional;

import com.clinique.gestion_clinique.api.model.DemandeExpertise;

public interface DemandeExpertiseRepository {
    DemandeExpertise save(DemandeExpertise demande);

    Optional<DemandeExpertise> findById(Long id);

    List<DemandeExpertise> findBySpecialisteId(Long specialisteId);

    Optional<DemandeExpertise> findByConsultationId(Long consultationId);
}
