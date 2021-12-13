package com.msaid.stockservice.mongo.doc;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.msaid.stockservice.dto.MonthlyStatistic;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    private String username;
    @Builder.Default
    private Map<String,MonthlyStatistic> monthlyStatistics = new HashMap<>();
}
