package com.msaid.stockservice.controller;

import com.msaid.stockservice.dto.OrderDto;
import com.msaid.stockservice.mongo.doc.Order;
import com.msaid.stockservice.service.OrderService;
import com.msaid.stockservice.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@Log4j2
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final StatisticService statisticService;

    //Will persist new order
    @PutMapping("/new")
    public CompletableFuture<ResponseEntity<Order>> insertNewOrder(@RequestBody  @Valid OrderDto order){
        return orderService.insertNewOrder(order)
                .thenCompose(order1 -> statisticService
                        .insertStatistic(order1)
                        .thenApply(aBoolean -> order1))
                .thenApply(ResponseEntity::ok);
    }

    //Will query order by Id
    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity> getOrderById(@PathVariable long orderId){
        return orderService.getOrderById(orderId)
                .thenApply(ResponseEntity::ok);
    }

    //List orders by date interval

}
