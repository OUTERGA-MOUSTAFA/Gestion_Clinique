package com.clinique.teleexpertise.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages(
                "com.clinique.teleexpertise.resource",
                "com.clinique.teleexpertise.filter",
                "com.clinique.teleexpertise.exception");
        register(RolesAllowedDynamicFeature.class);
    }
}
