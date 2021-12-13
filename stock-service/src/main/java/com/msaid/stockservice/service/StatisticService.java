package com.msaid.stockservice.service;

import com.msaid.stockservice.dto.MonthlyStatistic;
import com.msaid.stockservice.mongo.doc.Order;
import com.msaid.stockservice.mongo.doc.Statistic;
import com.msaid.stockservice.mongo.repo.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticsRepository statisticsRepository;


    public CompletableFuture<Boolean> insertStatistic(Order order) {
        LocalDate localDate = order.getOrderDate()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String mapKey = localDate.toString();
        return statisticsRepository.findById(order.getUsername())
                .switchIfEmpty(Mono.just(Statistic.builder()
                        .username(order.getUsername())
                        .build()))
                .flatMap(statistic -> {
                    MonthlyStatistic monthlyStatistic = statistic.getMonthlyStatistics().get(mapKey);

                    if (Optional.ofNullable(monthlyStatistic).isEmpty()) {
                        monthlyStatistic = MonthlyStatistic.builder()
                                .totalPurchasedAmount(order.getTotalPrice())
                                .totalOrderCount(1)
                                .totalBookCount(order.getTotalBookCount())
                                .build();

                    } else {
                        int totalOrderCount = monthlyStatistic.getTotalOrderCount() + 1;
                        BigDecimal totalPurchasedAmount = monthlyStatistic.getTotalPurchasedAmount().add(order.getTotalPrice());
                        int totalBookCount = monthlyStatistic.getTotalBookCount() + order.getTotalBookCount();

                        monthlyStatistic.setTotalOrderCount(totalOrderCount);
                        monthlyStatistic.setTotalPurchasedAmount(totalPurchasedAmount);
                        monthlyStatistic.setTotalBookCount(totalBookCount);
                    }

                    statistic.getMonthlyStatistics().put(mapKey, monthlyStatistic);

                    return statisticsRepository.save(statistic);
                }).toFuture().thenApply(any -> true);

    }

    public CompletableFuture<Statistic> findByUsername(String username){
        return statisticsRepository.findById(username)
                .toFuture();
    }


}
