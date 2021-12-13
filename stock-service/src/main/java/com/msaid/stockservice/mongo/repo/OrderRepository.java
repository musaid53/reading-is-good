package com.msaid.stockservice.mongo.repo;

import com.msaid.stockservice.mongo.doc.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long > {
}
