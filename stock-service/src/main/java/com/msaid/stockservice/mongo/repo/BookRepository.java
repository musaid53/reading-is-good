package com.msaid.stockservice.mongo.repo;

import com.msaid.stockservice.mongo.doc.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ReactiveCrudRepository<Book, String > {
}
