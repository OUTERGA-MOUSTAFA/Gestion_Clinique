package test;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/me")
@Produces(MediaType.APPLICATION_JSON)

public class test {
    
    @Context
    private ContainerRequestContext requestContext;

    @GET
    @RolesAllowed({"GENERALISTE", "SPECIALISTE"})
    public Response me(){
        SecurityContext securityContext = requestContext.getSecurityContext();
        Utilisateur utilisateur = ((securityContext) securityContext.getUtilisateur());

        return Response.ok(Map.of(
            "email", utilisateur.getEmail(),
            "nom", utilisateur.getNom(),
            "prenom", utilisateur.getPrenom(),
            "role", utilisateur.getRole().name()
        )).build();
    }
}
