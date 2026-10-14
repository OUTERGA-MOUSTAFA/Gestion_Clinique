package com.clinique.gestion_clinique.api.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.clinique.gestion_clinique.api.exception.ApiExceptionMapper;
import com.clinique.gestion_clinique.api.exception.JsonExceptionMapper;
import com.clinique.gestion_clinique.api.exception.JsonMappingExceptionMapper;
import com.clinique.gestion_clinique.api.security.BasicAuthFilter;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        packages(
                "com.clinique.gestion_clinique.api.resource",
                "com.clinique.gestion_clinique.api.exception");
        register(JacksonFeature.class);
        register(ObjectMapperProvider.class);
        register(BasicAuthFilter.class);
        register(RolesAllowedDynamicFeature.class);
        register(ApiExceptionMapper.class);
        register(JsonExceptionMapper.class);
        register(JsonMappingExceptionMapper.class);
    }
}
