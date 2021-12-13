package com.msaid.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.msaid.stockservice.mongo.doc.Order;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class PagedOrderDto {
    private String username;
    private int pageNumber;
    private int pageSize;
    private List<Order> orders;
}
