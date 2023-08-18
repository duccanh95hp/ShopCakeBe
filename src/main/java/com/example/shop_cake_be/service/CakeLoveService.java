package com.example.shop_cake_be.service;

import com.example.shop_cake_be.dto.CakeDto;

import java.util.List;

public interface CakeLoveService {
    boolean create(Long cakeId);
    List<CakeDto> getCakeLoveByUser();
    boolean delete(Long cakeId);
}
