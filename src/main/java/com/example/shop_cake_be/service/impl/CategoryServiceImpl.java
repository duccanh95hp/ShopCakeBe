package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.models.Category;
import com.example.shop_cake_be.payload.CategoryPayload;
import com.example.shop_cake_be.repository.CategoryRepo;
import com.example.shop_cake_be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo categoryRepo;
    @Override
    public Category create(Category category) {
        Category cat = new Category();
        cat.setName(category.getName());
        cat.setDescription(category.getDescription());
        cat.setStatus(1);
        cat.setIsDeleted(1);
        cat.setCreatedAt(new Date());
        cat.setUpdatedAt(new Date());
        categoryRepo.save(cat);
        return cat;
    }

    @Override
    public com.example.shop_cake_be.common.Page<Object> getAllAndSearch(CategoryPayload filter) {
        Page<Category> page = categoryRepo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>(page.getContent());
        return com.example.shop_cake_be.common.Page.builder()
                .result(objectList)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(filter.getSize())
                .pageNumber(filter.getPage())
                .build();
    }

    @Override
    public Optional<Category> findById(long id) {
        return categoryRepo.findById(id);
    }

    @Override
    public Optional<Category> update(long id,Category category) {
        Optional<Category> cat = categoryRepo.findById(id);
        if(cat.isEmpty()) {
            return Optional.empty();
        }
        cat.get().setName(category.getName());
        cat.get().setDescription(category.getDescription());
        cat.get().setUpdatedAt(new Date());
        categoryRepo.save(cat.get());
        return cat;
    }

    @Override
    public boolean delete(long id) {
        Optional<Category> category = categoryRepo.findById(id);
        if(category.isEmpty()) {
            return false;
        }
        category.get().setIsDeleted(0);
        categoryRepo.save(category.get());
        return true;
    }

    @Override
    public boolean procedure(long id, CategoryPayload categoryPayload) {
        Optional<Category> category = categoryRepo.findById(id);
        if(category.isEmpty()) {
            return false;
        }
        switch (categoryPayload.getProcedure()) {
            case "hoạt động":
                category.get().setStatus(1);
                categoryRepo.save(category.get());
                break;
            default:
                category.get().setStatus(0);
                categoryRepo.save(category.get());
                break;
        }
        return true;
    }

}
