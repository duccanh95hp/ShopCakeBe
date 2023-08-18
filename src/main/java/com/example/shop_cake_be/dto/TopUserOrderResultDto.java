package com.example.shop_cake_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUserOrderResultDto {
    private Long userId;
    private Long quantity;
}
