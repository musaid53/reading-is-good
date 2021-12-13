package com.msaid.stockservice.mongo.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.msaid.stockservice.dto.BookOrderDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private Long orderId ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date orderDate;
    @Indexed
    private String username;
    private List<BookOrderDto> books;
    private BigDecimal totalPrice;

    public int getTotalBookCount(){
        return books.stream().map(BookOrderDto::getQuantity).reduce(0, Integer::sum);
    }

}
