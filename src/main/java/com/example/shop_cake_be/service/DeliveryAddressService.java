package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.DeliveryAddress;
import com.example.shop_cake_be.payload.DeliveryAddressPayload;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressService {
    DeliveryAddress create(DeliveryAddress address);
    Page<Object> getAllAndSearch(DeliveryAddressPayload filter);
    Optional<DeliveryAddress> findById(long id);
    Optional<DeliveryAddress> update(long id, DeliveryAddress model);
    boolean delete(long id);
    boolean procedure(long id, DeliveryAddressPayload payload);
    List<DeliveryAddress> findByUserId();
}
