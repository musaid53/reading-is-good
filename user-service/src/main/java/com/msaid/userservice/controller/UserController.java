package com.msaid.userservice.controller;

import com.msaid.common.dto.AuthRequest;
import com.msaid.userservice.mongo.doc.User;
import com.msaid.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("save")
    public CompletableFuture<ResponseEntity<User>> save (@RequestBody User user){
        return userService.saveUser(user)
                .thenApply(ResponseEntity::ok);
    }


    @PostMapping("get-user")
    public CompletableFuture<ResponseEntity<User>> getUserByUsername(@RequestBody AuthRequest authRequest){
        return userService.getByUsernamePassword(authRequest.getUsername(), authRequest.getPassword())
                .thenApply(user -> {
                    if(Optional.ofNullable(user).isPresent())
                        return ResponseEntity.ok(user);

                    return ResponseEntity.badRequest().build();
                });
    }

    @GetMapping("get-user/{username}")
    public CompletableFuture<ResponseEntity<User>> getUserByUsername(@PathVariable String username){
        return userService.getByUserName(username)
                .thenApply(user -> {
                    if(Optional.ofNullable(user).isPresent())
                        return ResponseEntity.ok(user);

                    return ResponseEntity.badRequest().build();
                });
    }

    @PutMapping("make-admin/{username}")
     public CompletableFuture<ResponseEntity<User>> addAdminRoleToUser (@PathVariable("username") String username){
        return userService.addAdminRoleToUser(username).thenApply(ResponseEntity::ok);
    }

}
