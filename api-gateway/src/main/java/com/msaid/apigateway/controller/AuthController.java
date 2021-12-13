package com.msaid.apigateway.controller;

import com.msaid.apigateway.dto.AuthRequest;
import com.msaid.apigateway.dto.AuthResponse;
import com.msaid.apigateway.service.AuthService;
import com.msaid.common.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/message")
    public String message(){
        return "HelloĞĞ";
    }

//    @PostMapping("register")
//    public AuthResponse register(AuthRequest authRequest){
//
//    }

    @GetMapping("/apig/{username}")
    public CompletableFuture<ResponseEntity<UserDto>> getUser(@PathVariable("username") String username){
        return authService.getUser(username).thenApply(ResponseEntity::ok);
    }

    @PostMapping("register")
    public CompletableFuture<ResponseEntity<AuthResponse>> register(@RequestBody AuthRequest authRequest){
        return authService.register(authRequest).thenApply(ResponseEntity::ok);
    }

}
