package com.example.shop_cake_be.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderFilter {
    private long userId;
    private int status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int size;
    private int page;
    private int limit;
}
