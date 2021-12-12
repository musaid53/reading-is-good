package com.msaid.userservice.service;

import com.msaid.userservice.exception.UserNotFoundException;
import com.msaid.userservice.mongo.doc.User;
import com.msaid.userservice.mongo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public CompletableFuture<User> saveUser(User user){
        return userRepository.findById(user.getUsername())
                        .switchIfEmpty(Mono.defer(() -> userRepository.save(user)))
                .toFuture();
    }
    public CompletableFuture<User> getByUsername(String username){
        return userRepository.findById(username)
                .toFuture();

    }

    public CompletableFuture<User> addAdminRoleToUser(String username){
        return userRepository.findById(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("No user Found")))
                .flatMap(user -> {
                    if(!user.isAdmin()){
                        user.getRoles().add("admin");
                        return userRepository.save(user);
                    }
                    return Mono.just(user);
                }).toFuture();
    }

    public CompletableFuture<User> deleteAdminRoleToUser(String username){
        return userRepository.findById(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("No user Found")))
                .flatMap(user -> {
                    if(!user.isAdmin()){
                        user.getRoles().remove("admin");
                        return userRepository.save(user);
                    }
                    return Mono.just(user);
                }).toFuture();
    }

}
