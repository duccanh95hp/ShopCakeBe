package com.example.shop_cake_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartTmtDto {
    private Long ShoppingCartTmtId;
    private Long cakeId;
    private String nameCake;
    private String imageCake;
    private Double priceCake;
    private Integer quantityShoppingCartTmt;
}
