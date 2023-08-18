package com.example.shop_cake_be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PromotionAddOrDelPayLoad {
    private Long promotionId;
    private int type;
    // 1 : thêm , 2 : xóa
    private Long[] listCakeId;
}
