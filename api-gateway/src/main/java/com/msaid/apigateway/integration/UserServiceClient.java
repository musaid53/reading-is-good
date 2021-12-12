package com.msaid.apigateway.integration;

import com.msaid.apigateway.config.JwtUtil;
import com.msaid.apigateway.dto.AuthRequest;
import com.msaid.apigateway.dto.AuthResponse;
import com.msaid.apigateway.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private WebClient webClient;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init(){
        webClient = WebClient.builder()
                .baseUrl("http://host.docker.internal:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public CompletableFuture<User> getUsers(String userName){
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/get-user/{id}").build(userName))
                .retrieve()
                .bodyToMono(User.class).toFuture();
    }


    public CompletableFuture<AuthResponse> register(AuthRequest authRequest){
        User user = User.builder()
                .username(authRequest.getPassword())
                .password(authRequest.getPassword())
                .roles(Set.of("customer"))
                .build();
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/save").build())
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class).toFuture().thenApply(savedUser -> AuthResponse.builder()
                        .bearer(jwtUtil.generate(user))
                        .build());
    }





}
