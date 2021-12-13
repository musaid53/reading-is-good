package com.msaid.stockservice.controller;

import com.msaid.stockservice.dto.OrderDto;
import com.msaid.stockservice.dto.OrderIntervalReq;
import com.msaid.stockservice.dto.PagedOrderDto;
import com.msaid.stockservice.mongo.doc.Order;
import com.msaid.stockservice.service.OrderService;
import com.msaid.stockservice.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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
    public CompletableFuture<ResponseEntity<Order>> getOrderById(@PathVariable long orderId){
        return orderService.getOrderById(orderId)
                .thenApply(ResponseEntity::ok);
    }

    //List orders by date interval
    @PostMapping("/by-date")
    public CompletableFuture<ResponseEntity<List<Order>>> getOrderByDateInterval(@RequestBody OrderIntervalReq orderIntervalReq){
        return orderService.getOrderByDate(orderIntervalReq.getStarDate(), orderIntervalReq.getEndDate())
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = {"/by-username/{username}", "/by-username/{username}/{pageNumber}"})
    public CompletableFuture<ResponseEntity<PagedOrderDto>> getOrderByDateInterval(@PathVariable String username, @PathVariable(required = false) Integer pageNumber){
        if(Optional.ofNullable(pageNumber).isEmpty() || pageNumber < 0)
            pageNumber = 0;
        return orderService.getOrdersByUserName(username,pageNumber)
                .thenApply(ResponseEntity::ok);
    }

}
