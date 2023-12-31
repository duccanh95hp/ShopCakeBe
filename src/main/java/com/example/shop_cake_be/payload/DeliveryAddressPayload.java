package com.example.shop_cake_be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DeliveryAddressPayload {
    private String name;
    private int size;
    private int page;
    private String address;
    private String procedure;
    private Long userId;
}