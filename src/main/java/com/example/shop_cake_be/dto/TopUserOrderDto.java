package com.example.shop_cake_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUserOrderDto {
    private Long idUser;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Long quantity;
    private Double totalMoney;
}
