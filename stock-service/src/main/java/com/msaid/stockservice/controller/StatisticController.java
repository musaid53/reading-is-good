package com.msaid.stockservice.controller;

import com.msaid.stockservice.mongo.doc.Statistic;
import com.msaid.stockservice.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequestMapping("statistic")
@RequiredArgsConstructor
@RestController
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/{username}")
    public CompletableFuture<ResponseEntity<Statistic>> getStatistic(@PathVariable String username){
        return statisticService.findByUsername(username)
                .thenApply(ResponseEntity::ok);
    }
}
