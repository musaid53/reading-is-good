package com.msaid.apigateway.filters;

import com.msaid.apigateway.config.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@Log4j2
public class AuthCheckFilter  implements GatewayFilter {
    private static final List<String> NON_SECURE_APIS = List.of("/register", "/login");
    private static Predicate<ServerHttpRequest> isSecure =
            serverHttpRequest -> NON_SECURE_APIS.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

    @Value("${serviceclient.secret:secret}")
    private String serviceSecret;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (!isServiceCall(request)) {
            if (isAuthMissing(request))
                return onError(exchange, HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            if (jwtUtil.isInvalid(token))
                return onError(exchange, HttpStatus.UNAUTHORIZED);

        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange,HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0).replace("Bearer ", "");
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private boolean isServiceCall(ServerHttpRequest request){
        if(Optional.ofNullable(request.getHeaders().get("service-secret")).isPresent() &&
                serviceSecret.equals(request.getHeaders().get("service-secret").get(0)))
            return true;
        return false;
    }

}
