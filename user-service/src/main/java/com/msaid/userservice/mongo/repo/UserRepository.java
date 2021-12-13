package com.msaid.userservice.mongo.repo;

import com.msaid.userservice.mongo.doc.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsernameAndPassword(String username, String password);
}
