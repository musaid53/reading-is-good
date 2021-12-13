package com.msaid.stockservice.controller;

import com.msaid.stockservice.dto.StockUpdateReq;
import com.msaid.stockservice.mongo.doc.Book;
import com.msaid.stockservice.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("book")
@Validated
public class BookController {

    private final BookService bookService;

    //persist new book
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Book>> addBook(@RequestBody @Valid Book book){
        return bookService.upsert(book)
                .thenApply(ResponseEntity::ok);
    }

    // Update book stock
    @PutMapping("/update-stock")
    public CompletableFuture<ResponseEntity<Book>> updateBookStock(@RequestBody @Valid StockUpdateReq stockUpdateReq){
        return bookService.updateBookStock(stockUpdateReq)
                .thenApply(ResponseEntity::ok);
    }

}
