package com.msaid.stockservice.mongo.repo;

import com.msaid.stockservice.mongo.doc.Statistic;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StatisticsRepository extends ReactiveCrudRepository<Statistic, String> {
}
