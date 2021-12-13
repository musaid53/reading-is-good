package com.msaid.stockservice;

import com.msaid.stockservice.mongo.doc.Book;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface ErrorUtil {
    default <T>CompletableFuture<T> handleErrorCF(String message){
        CompletableFuture<T> err = new CompletableFuture<>();
        err.completeExceptionally(new RuntimeException(message));
        return err;
    }

    default <T> Mono<T> handleErrorMono(String message){
        return Mono.error(new RuntimeException(message));
    }
}
