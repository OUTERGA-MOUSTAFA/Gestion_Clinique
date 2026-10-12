package com.clinique.gestion_clinique.api.model;

/**
 * Enum de la priorité d'une demande d'expertise.
 *
 * L'ordre déclaré ici correspond à l'ordre naturel utilisé pour le tri
 * par priorité (ordinal 0 = URGENTE est affiché en premier).
 */
public enum Priorite {
    URGENTE,
    NORMALE,
    NON_URGENTE
}
