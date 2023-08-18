package com.example.shop_cake_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailStatusDto {
    private int status;
    private List<OrderDetailDto> detailDtoList;
}
