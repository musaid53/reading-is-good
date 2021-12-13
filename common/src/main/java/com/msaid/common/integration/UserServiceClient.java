package com.msaid.common.integration;

import com.msaid.common.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Component
@Getter
@RequiredArgsConstructor
public class UserServiceClient {

    @Value("${user-service.baseurl:http://host.docker.internal:8082}")
    private String userServiceBaseUrl;

    @Value("${serviceclient.secret}")
    private String serviceSecret;

    private WebClient webClient;

    @PostConstruct
    public void init(){

        webClient = WebClient.builder()
                .baseUrl(userServiceBaseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add("service-secret",serviceSecret);
                })
                .build();
    }

    public CompletableFuture<UserDto> getUser(String userName){
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/get-user/{id}").build(userName))
                .retrieve()
                .bodyToMono(UserDto.class).toFuture();
    }






}
