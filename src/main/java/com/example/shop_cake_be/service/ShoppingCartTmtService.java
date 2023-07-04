package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.ShoppingCartTmt;
import com.example.shop_cake_be.payload.ShoppingCartTmtPayload;

import java.util.Optional;

public interface ShoppingCartTmtService {
    ShoppingCartTmt create(ShoppingCartTmt model);
    Page<Object> getAllAndSearch(ShoppingCartTmtPayload filter);

    ShoppingCartTmt update(long id, ShoppingCartTmt model);
    boolean delete(long id);
}
