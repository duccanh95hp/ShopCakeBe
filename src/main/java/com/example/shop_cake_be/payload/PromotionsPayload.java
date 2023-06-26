package com.example.shop_cake_be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PromotionsPayload {
    private String name;
    private int size;
    private int page;
    private String procedure;

    private Double discount;
    private String startDate;
    private String endDate;
}
