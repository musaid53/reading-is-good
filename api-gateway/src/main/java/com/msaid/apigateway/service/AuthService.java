package com.msaid.apigateway.service;

import com.msaid.apigateway.config.JwtUtil;
import com.msaid.common.dto.AuthRequest;
import com.msaid.apigateway.dto.AuthResponse;
import com.msaid.apigateway.dto.User;
import com.msaid.common.dto.UserDto;
import com.msaid.common.integration.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;
    public CompletableFuture<UserDto> getUser(String userName){
        return userServiceClient.getUser(userName);
    }

    public CompletableFuture<AuthResponse> register(AuthRequest authRequest){
        User user = User.builder()
                .username(authRequest.getUsername())
                .password(authRequest.getPassword())
                .roles(Set.of("customer"))
                .build();
        return userServiceClient.getWebClient()
                .post()
                .uri(uriBuilder -> uriBuilder.path("/save").build())
                .bodyValue(user)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> Mono.just(new RuntimeException("Same user exists")))
                .bodyToMono(User.class).toFuture().thenApply(savedUser -> AuthResponse.builder()
                        .bearer(jwtUtil.generate(savedUser))
                        .build());
    }

    public CompletableFuture<AuthResponse> generateToken(AuthRequest authRequest){
        return userServiceClient.getWebClient()
                .post()
                .uri(uriBuilder -> uriBuilder.path("/get-user").build())
                .bodyValue(authRequest)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> Mono.just(new RuntimeException("User not found")))
                .bodyToMono(User.class).toFuture().thenApply(savedUser -> AuthResponse.builder()
                        .bearer(jwtUtil.generate(savedUser))
                        .build());
    }

}
