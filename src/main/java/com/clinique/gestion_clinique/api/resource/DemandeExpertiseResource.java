package com.clinique.gestion_clinique.api.resource;

import com.clinique.gestion_clinique.api.dto.CreateDemandeRequest;
import com.clinique.gestion_clinique.api.dto.DemandeDto;
import com.clinique.gestion_clinique.api.dto.ReponseRequest;
import com.clinique.gestion_clinique.api.exception.ApiException;
import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.security.SecurityUserContext;
import com.clinique.gestion_clinique.api.service.DemandeService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Resource REST pour les demandes d'expertise médicale.
 *
 * Endpoints :
 *
 *   POST   /api/demandes                          → créer une demande      (GENERALISTE)
 *   GET    /api/demandes?statut=EN_ATTENTE         → liste pour spécialiste (SPECIALISTE)
 *   GET    /api/demandes?consultationId=12         → demande par consultation (GENERALISTE)
 *   PUT    /api/demandes/{id}/reponse              → répondre               (SPECIALISTE)
 */
@Path("/demandes")
@Produces(MediaType.APPLICATION_JSON)
public class DemandeExpertiseResource {

    /**
     * SecurityContext injecté automatiquement par Jersey.
     * Contient l'utilisateur authentifié par BasicAuthFilter.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * Service métier. DemandeService gère lui-même son EntityManager
     * via JpaUtil, donc on n'en passe pas au constructeur.
     */
    private final DemandeService demandeService = new DemandeService();

    // =========================================================================
    // US2 : POST /api/demandes — Créer une demande d'expertise
    // =========================================================================

    /**
     * Crée une nouvelle demande d'expertise.
     *
     * Le corps JSON attendu :
     * {
     *   "consultationId": 12,
     *   "specialisteId": 3,
     *   "question": "ECG anormal...",
     *   "priorite": "URGENTE"
     * }
     *
     * Codes retournés :
     *   201 Created  → demande créée
     *   400          → question vide ou priorité invalide
     *   404          → spécialiste ou consultation introuvable
     */
    @POST
    @RolesAllowed("GENERALISTE")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creer(CreateDemandeRequest request) {
        try {
            DemandeDto demande = demandeService.creer(request);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(demande)
                    .build();
        } catch (ApiException e) {
            return Response
                    .status(e.getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

    // =========================================================================
    // US3 : GET /api/demandes — Consulter les demandes
    // =========================================================================

    /**
     * Retourne les demandes selon le rôle de l'utilisateur connecté.
     *
     * - SPECIALISTE : ?statut=EN_ATTENTE  → ses propres demandes, triées par priorité
     * - GENERALISTE : ?consultationId=12  → la demande liée à cette consultation
     *
     * Codes retournés :
     *   200 OK
     *   400 → paramètre manquant ou invalide
     *   404 → demande introuvable (généraliste)
     */
    @GET
    @RolesAllowed({"GENERALISTE", "SPECIALISTE"})
    public Response lister(
            @QueryParam("statut") String statut,
            @QueryParam("consultationId") Long consultationId) {

        try {
            Utilisateur utilisateur = utilisateurConnecte();

            // Le spécialiste voit ses propres demandes
            if (securityContext.isUserInRole("SPECIALISTE")) {
                return Response.ok(
                        demandeService.listerPourSpecialiste(utilisateur, statut)
                ).build();
            }

            // Le généraliste cherche par consultationId
            return Response.ok(
                    demandeService.trouverParConsultation(consultationId)
            ).build();

        } catch (ApiException e) {
            return Response
                    .status(e.getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

    // =========================================================================
    // US4 : PUT /api/demandes/{id}/reponse — Répondre à une demande
    // =========================================================================

    /**
     * Le spécialiste saisit son avis et ses recommandations.
     * La demande passe au statut TERMINEE.
     *
     * Corps JSON attendu :
     * {
     *   "avis": "...",
     *   "recommandations": "..."
     * }
     *
     * Codes retournés :
     *   200 OK  → réponse enregistrée
     *   400     → avis ou recommandations vides
     *   403     → demande adressée à un autre spécialiste
     *   404     → demande introuvable
     */
    @PUT
    @Path("/{id}/reponse")
    @RolesAllowed("SPECIALISTE")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response repondre(
            @PathParam("id") Long id,
            ReponseRequest request) {

        try {
            Utilisateur utilisateur = utilisateurConnecte();
            DemandeDto demande = demandeService.repondre(id, utilisateur, request);
            return Response.ok(demande).build();
        } catch (ApiException e) {
            return Response
                    .status(e.getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalArgumentException exception) {
            return Response.status(Response.Status.BAD_REQUEST).entity(exception).build();
        }
    }

    // =========================================================================
    // Méthode utilitaire privée
    // =========================================================================

    /**
     * Extrait l'objet Utilisateur depuis le SecurityContext.
     * Le SecurityContext a été renseigné par BasicAuthFilter.
     */
    private Utilisateur utilisateurConnecte() {
        return ((SecurityUserContext) securityContext).getUtilisateur();
    }

    @PUT
    @Path("/{id}/reponse")
    @RolesAllowed("SPECIALISTE")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response repondre(@PathParam("id") Long id, ResponseDemandeRequest request) {

        try {

            String email = securityContext.getUserPrincipal().getName();

            DemandeExpertise demande = demandeService.repondreDemandeExpertise(
                    id,
                    request,
                    email);

            return Response
                    .ok(demande)
                    .build();

        } catch (IllegalArgumentException exception) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(exception.getMessage())
                    .build();

        } catch (SecurityException exception) {

            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(exception.getMessage())
                    .build();
        }
    }

}