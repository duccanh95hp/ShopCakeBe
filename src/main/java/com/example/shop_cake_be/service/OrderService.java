package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.dto.OrderDetailDto;
import com.example.shop_cake_be.models.Order;
import com.example.shop_cake_be.payload.OrderPayload;

import java.util.List;

public interface OrderService {
    Order create(OrderPayload model);
    Page<Object> getAllAndSearch(OrderPayload filter);
    List<OrderDetailDto> detailOrder(long orderId);
    boolean procedure(long orderId, OrderPayload payload);
}
