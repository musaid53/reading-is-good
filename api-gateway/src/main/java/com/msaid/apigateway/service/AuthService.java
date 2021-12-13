package com.msaid.apigateway.service;

import com.msaid.apigateway.config.JwtUtil;
import com.msaid.apigateway.dto.AuthRequest;
import com.msaid.apigateway.dto.AuthResponse;
import com.msaid.apigateway.dto.User;
import com.msaid.common.dto.UserDto;
import com.msaid.common.integration.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                .username(authRequest.getPassword())
                .password(authRequest.getPassword())
                .roles(Set.of("customer"))
                .build();
        return userServiceClient.getWebClient()
                .post()
                .uri(uriBuilder -> uriBuilder.path("/save").build())
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class).toFuture().thenApply(savedUser -> AuthResponse.builder()
                        .bearer(jwtUtil.generate(user))
                        .build());
    }
}
