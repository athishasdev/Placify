package com.placify.config;

// CORS configuration is handled inside SecurityConfig.java
// This class is intentionally left empty to avoid duplicate bean conflicts.

import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {
    // All CORS config is in SecurityConfig.corsConfigurationSource()
}
