package com.msaid.stockservice.mongo.repo;

import com.msaid.stockservice.mongo.doc.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, Long > {

    Flux<Order> findByOrderDateBetween(Date startDate, Date endDate);

    Flux<Order> findByUsername(String username, Pageable pageable);
}
