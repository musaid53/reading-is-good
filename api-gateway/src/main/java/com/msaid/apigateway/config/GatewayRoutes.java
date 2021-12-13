package com.msaid.apigateway.config;

import com.msaid.apigateway.filters.AuthCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayRoutes {

    @Autowired
    private AuthCheckFilter authCheckFilter;

    @Bean
    public RouteLocator route(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route("user-service", predicateSpec ->
                    predicateSpec.path("/user/**")
                            .filters(f -> f.rewritePath("/user/(?<path>.*)", "/${path}")
                                    .filter(authCheckFilter))
                            .uri("lb://user-service")
                )
                .route("stock-service", predicateSpec ->
                        predicateSpec.path("/stock/**")
                                .filters(f -> f.rewritePath("/stock/(?<path>.*)", "/${path}")
                                        .filter(authCheckFilter))
                                .uri("lb://stock-service")
                )
                .build();
    }
}
