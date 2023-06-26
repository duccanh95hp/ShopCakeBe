package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.models.CakePromotion;
import com.example.shop_cake_be.payload.CakePayload;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CakeService {
    Cake create(Cake model);
    Page<Object> getAllAndSearch(CakePayload filter);
    Optional<Cake> findById(long id);
    Optional<Cake> update(long id, Cake model);
    boolean delete(long id);
    boolean createPromotion(CakePromotion payload);
    boolean procedure(long id, CakePayload cakePayload);
}
