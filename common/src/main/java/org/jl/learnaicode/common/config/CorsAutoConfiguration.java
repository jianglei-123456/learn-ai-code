package org.jl.learnaicode.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnProperty(prefix = "common.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CorsProperties.class)
public class CorsAutoConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer(CorsProperties properties) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(properties.getAllowedOrigins().toArray(new String[0]))
                        .allowedHeaders(properties.getAllowedHeaders().toArray(new String[0]))
                        .allowedMethods(properties.getAllowedMethods().toArray(new String[0]))
                        .allowCredentials(properties.isAllowCredentials())
                        .maxAge(properties.getMaxAge());
            }
        };
    }
}
