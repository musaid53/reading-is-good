package com.msaid.stockservice.service;

import com.msaid.common.integration.UserServiceClient;
import com.msaid.stockservice.ErrorUtil;
import com.msaid.stockservice.dto.BookOrderDto;
import com.msaid.stockservice.dto.OrderDto;
import com.msaid.stockservice.dto.PagedOrderDto;
import com.msaid.stockservice.mongo.doc.Order;
import com.msaid.stockservice.mongo.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements ErrorUtil {
    private final UserServiceClient userServiceClient;
    private final BookService bookService;
    private final OrderRepository orderRepository;


    @Transactional
    public CompletableFuture<Order> insertNewOrder(OrderDto orderDto){

        return userServiceClient.getUser(orderDto.getUsername())
                .thenCompose(userDto -> {
                    if (orderDto.getBooks().isEmpty()){
                        return handleErrorCF("You must define at least one book in order");
                    }
                    if (Optional.ofNullable(userDto).isEmpty()){
                        return handleErrorCF(String.format("No user found with username: %s", orderDto.getUsername()));
                    }

                    return purchaseBook(orderDto.getBooks())
                            .thenCompose(books -> {
                                Date date = new Date();
                                BigDecimal totalPrice = books.stream().map(BookOrderDto::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                                Order order = Order.builder()
                                        .orderId(date.getTime())
                                        .orderDate(date)
                                        .username(userDto.getUsername())
                                        .totalPrice(totalPrice)
                                        .books(books)
                                        .build();
                                return orderRepository.save(order).toFuture();
                            });
                });
    }

    private CompletableFuture<List<BookOrderDto>> purchaseBook(List<BookOrderDto> books){
          return CompletableFuture.supplyAsync(() -> books.stream()
                .map(book -> bookService.sellBook(book.getBookName(), book.getQuantity())
                        .thenApply(purchasedBook -> purchasedBook.toBookOrderDto(book.getQuantity()))).map(CompletableFuture::join).collect(Collectors.toList()));
    }

    public CompletableFuture<Order> getOrderById(long orderId){
        return orderRepository.findById(orderId)
                .switchIfEmpty(handleErrorMono(String.format("No order found given id: %s",orderId)))
                .toFuture();
    }

    public CompletableFuture<List<Order>> getOrderByDate(Date startDate, Date endDate){
        return orderRepository.findByOrderDateBetween(startDate, endDate)
                .collectList()
                .toFuture();
    }
    public CompletableFuture<PagedOrderDto> getOrdersByUserName(String username, int pageNumber){
        Pageable pageRequest = PageRequest.of(pageNumber, 50);
        return orderRepository.findByUsername(username, pageRequest)
                .collectList()
                .toFuture()
                .thenApply(orders -> PagedOrderDto.builder()
                        .orders(orders)
                        .pageNumber(pageNumber)
                        .pageSize(50)
                        .username(username)
                        .build());
    }

}
