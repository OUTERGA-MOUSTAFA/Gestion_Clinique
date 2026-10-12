package com.clinique.gestion_clinique.api.model;

/**
 * Enum du statut d'une demande d'expertise.
 *
 * EN_ATTENTE → demande créée, le spécialiste n'a pas encore répondu.
 * TERMINEE   → le spécialiste a fourni son avis et ses recommandations.
 */
public enum StatutDemande {
    EN_ATTENTE,
    TERMINEE
}
