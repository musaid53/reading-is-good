package com.msaid.userservice.controller;

import com.msaid.userservice.mongo.doc.User;
import com.msaid.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("save")
    public CompletableFuture<ResponseEntity<User>> save (@RequestBody User user){
        return userService.saveUser(user)
                .thenApply(ResponseEntity::ok);
    }


    @GetMapping("get-user/{username}")
    public CompletableFuture<ResponseEntity<User>> getUserByUsername(@PathVariable("username") String username){
        return userService.getByUsername(username)
                .thenApply(user -> {
                    if(Optional.ofNullable(user).isPresent())
                        return ResponseEntity.ok(user);

                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("makeAdmin/{username}")
     public CompletableFuture<ResponseEntity<User>> addAdminRoleToUser (@PathVariable("username") String username){
        return userService.addAdminRoleToUser(username).thenApply(ResponseEntity::ok);
    }

}
