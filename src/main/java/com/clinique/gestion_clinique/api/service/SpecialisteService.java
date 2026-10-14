package com.clinique.gestion_clinique.api.service;

import java.util.Comparator;
import java.util.List;

import com.clinique.gestion_clinique.api.config.JpaUtil;
import com.clinique.gestion_clinique.api.dto.SpecialisteDto;
import com.clinique.gestion_clinique.api.exception.ApiException;
import com.clinique.gestion_clinique.api.model.Specialiste;
import com.clinique.gestion_clinique.api.model.Specialite;
import com.clinique.gestion_clinique.api.repository.SpecialisteRepository;

import jakarta.persistence.EntityManager;

public class SpecialisteService {

    public List<SpecialisteDto> listerParSpecialite(String specialiteParam) {
        if (specialiteParam == null || specialiteParam.isBlank()) {
            throw new ApiException(400, "Le parametre specialite est obligatoire");
        }

        Specialite specialite;
        try {
            specialite = Specialite.valueOf(specialiteParam.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "La specialite n'existe pas");
        }

        EntityManager entityManager = JpaUtil.createEntityManager();
        try {
            List<Specialiste> specialistes = new SpecialisteRepository(entityManager).findAll();
            return specialistes.stream()
                    .filter(specialiste -> specialiste.getSpecialite() == specialite)
                    .sorted(Comparator.comparingDouble(Specialiste::getTarif))
                    .map(SpecialisteDto::from)
                    .toList();
        } finally {
            entityManager.close();
        }
    }
}
