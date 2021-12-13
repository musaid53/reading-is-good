package com.msaid.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotNull(message = "UserName must be present")
    private String username;
    @NotEmpty(message = "Orders must be present")
    private List< @Valid BookOrderDto> books;

}
