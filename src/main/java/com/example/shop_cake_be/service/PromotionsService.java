package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.Promotions;
import com.example.shop_cake_be.payload.PromotionAddOrDelPayLoad;
import com.example.shop_cake_be.payload.PromotionsPayload;
import org.springframework.stereotype.Service;


public interface PromotionsService {
    Promotions create(PromotionsPayload model);
    Page<Object> getAllAndSearch(PromotionsPayload filter);
    Promotions findById(long id);
    Promotions update(long id, PromotionsPayload payload);
    boolean delete(long id);
    boolean procedure(long id, PromotionsPayload promotionsPayload);
    boolean promotionAddOrDelCake(PromotionAddOrDelPayLoad payLoad);
}
