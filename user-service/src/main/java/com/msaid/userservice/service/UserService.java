package com.msaid.userservice.service;

import com.msaid.userservice.exception.UserNotFoundException;
import com.msaid.userservice.mongo.doc.User;
import com.msaid.userservice.mongo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void init(){
        // init first user as admin if not exists

        userRepository.findById("admin")
                .switchIfEmpty(Mono.defer(() -> {
                    User adminUser = User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .roles(Set.of("admin", "customer"))
                            .build();
                    return userRepository.save(adminUser);
                })).block(Duration.ofSeconds(50));

    }


    //return new UserNotFoundException(String.format("Same user with username: %s exists",user.getUsername()));
    public CompletableFuture<User> saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.findById(user.getUsername())
                                .doOnNext(sameUserWithUsername -> {
                                    throw new UserNotFoundException(String.format("Same user with username: %s exists", user.getUsername()));
                                }).switchIfEmpty(Mono.defer(() -> userRepository.save(user)))))
                .toFuture();
    }
    public CompletableFuture<User> getByUsernamePassword(String username, String password){
        return userRepository.findById(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .toFuture();

    }
    public CompletableFuture<User> getByUserName(String username){
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
