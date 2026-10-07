package com.clinique.gestion_clinique.api.security;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

import com.clinique.gestion_clinique.api.model.Utilisateur;
import com.clinique.gestion_clinique.api.repository.jpa.JpaUtilisateurRepository;
import com.clinique.gestion_clinique.api.service.UserService;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    private final UserService userService = new UserService(new JpaUtilisateurRepository());

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization == null || !authorization.startsWith("Basic ")) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String credentials;
        try {
            byte[] decoded = Base64.getDecoder().decode(authorization.substring("Basic ".length()));
            credentials = new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String[] parts = credentials.split(":", 2);
        if (parts.length != 2) {
            abortWithUnauthorized(requestContext);
            return;
        }

        Optional<Utilisateur> authenticatedUser = userService.authenticate(parts[0], parts[1]);
        if (authenticatedUser.isEmpty()) {
            abortWithUnauthorized(requestContext);
            return;
        }

        Utilisateur user = authenticatedUser.get();
        SecurityContext previousSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> user.getEmail();
            }

            @Override
            public boolean isUserInRole(String role) {
                return user.getRole() != null && user.getRole().name().equals(role);
            }

            @Override
            public boolean isSecure() {
                return previousSecurityContext != null && previousSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Basic";
            }
        });
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"TeleExpertise\"")
                        .build());
    }
}
