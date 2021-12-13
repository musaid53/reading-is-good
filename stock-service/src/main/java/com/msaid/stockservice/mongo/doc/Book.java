package com.msaid.stockservice.mongo.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.msaid.stockservice.dto.BookOrderDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Document
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @NotNull(message = "Bookname must be set")
    private String bookName;
    @Min(value = 0, message = "Minimum book quantiy must be greater than 0")
    @NotNull(message = "Quantity must be set")
    private int quantity;
    @Min( value = 1, message = "Price must bigger than 1")
    @NotNull(message = "Price must be set")
    private BigDecimal price;


    public BookOrderDto toBookOrderDto(int quantity){
        return BookOrderDto.builder()
                .bookName(bookName)
                .price(price.multiply(BigDecimal.valueOf(quantity)))
                .quantity(quantity)
                .build();
    }
}
