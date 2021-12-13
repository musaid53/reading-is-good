package com.msaid.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class BookOrderDto {
    @NotNull(message = "Bookname must be set")
    private String bookName;
    @Min(value = 1, message = "Minimum book quantiy must be greater than 1")
    @NotNull(message = "Quantity must be set")
    private int quantity;
    private BigDecimal price;

}
