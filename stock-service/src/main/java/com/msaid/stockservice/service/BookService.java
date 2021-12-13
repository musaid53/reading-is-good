package com.msaid.stockservice.service;

import com.msaid.stockservice.ErrorUtil;
import com.msaid.stockservice.dto.StockUpdateReq;
import com.msaid.stockservice.mongo.doc.Book;
import com.msaid.stockservice.mongo.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookService implements ErrorUtil {

    private final BookRepository bookRepository;

    public CompletableFuture<Book> upsert(Book book){
        return bookRepository.save(book)
                .toFuture();
    }

    public CompletableFuture<Book> updateBookStock(StockUpdateReq stockUpdateReq) {
        return bookRepository.findById(stockUpdateReq.getBookName())
                .flatMap(book -> {
                    int quantity = book.getQuantity();
                    if(quantity + stockUpdateReq.getQuantity() < 0){
                        return Mono.error(new RuntimeException("Decreasing quantity is now allowed, requested quantity is bigger than actual"));
                    }
                    book.setQuantity(quantity + stockUpdateReq.getQuantity());
                    return bookRepository.save(book);
                }).switchIfEmpty(Mono.defer(() -> {
                    if (stockUpdateReq.getQuantity() < -1)
                        return Mono.error(new RuntimeException("New books quantity must be bigger than 0"));

                    return bookRepository.save(Book.builder()
                            .bookName(stockUpdateReq.getBookName())
                            .quantity(stockUpdateReq.getQuantity())
                            .build());
                })).toFuture();
    }

    @Transactional
    public CompletableFuture<Book> sellBook(String bookName, int quantity){
        if (quantity <= 0){
            return handleErrorCF("Quantity must be bigger than 0 for book" + bookName);
        }
        return bookRepository.findById(bookName)
//                .switchIfEmpty(Mono.error(new RuntimeException("No book found with name" + bookName))
                .toFuture()
                .thenCompose(book -> {
                    if (Optional.ofNullable(book).isEmpty())
                        return handleErrorCF(String.format("No book found with name %s", bookName));
                    if (book.getQuantity() < quantity)
                        return handleErrorCF(String.format("Book: %s quantity (%s) is smaller than requested, requested: %s", bookName, book.getQuantity(), quantity));
                    book.setQuantity(book.getQuantity() - quantity);
                    return bookRepository.save(book).toFuture();
                });
    }


}
