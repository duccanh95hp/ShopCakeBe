package com.example.shop_cake_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String name;
    private String email;
    private String orderCode;
    private String deliveryAddress;
    private String phone;
    private int status;
    private LocalDateTime deliveryDate;
    private LocalDateTime createdAt;
}
