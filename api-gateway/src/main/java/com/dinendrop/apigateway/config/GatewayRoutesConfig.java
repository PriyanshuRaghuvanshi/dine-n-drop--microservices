package com.dinendrop.apigateway.config;

import com.dinendrop.apigateway.auth.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Autowired
    private AuthenticationFilter authFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Auth Service - no auth needed
                .route("c", r -> r
                        .path("/auth/**")
                        .uri("lb://auth-service"))

                // Restaurant Service - needs auth
                .route("restaurant-service", r -> r
                        .path("/restaurants/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://restaurant-service"))

                .route("dining-service", r -> r
                        .path("/dining/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://dining-service"))

                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://order-service"))
                .build();
    }
}
