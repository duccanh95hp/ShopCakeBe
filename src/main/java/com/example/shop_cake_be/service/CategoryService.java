package com.example.shop_cake_be.service;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.Category;
import com.example.shop_cake_be.payload.CategoryPayload;

import java.util.Optional;

public interface CategoryService {
    Category create(Category category);
    Page<Object> getAllAndSearch(CategoryPayload filter);
    Optional<Category> findById(long id);
    Optional<Category> update(long id, Category category);
    boolean delete(long id);
    boolean procedure(long id, CategoryPayload categoryPayload);
}
