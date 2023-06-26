package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.Promotions;
import com.example.shop_cake_be.payload.PromotionsPayload;
import com.example.shop_cake_be.repository.PromotionsRepo;
import com.example.shop_cake_be.service.PromotionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionsServiceImpl implements PromotionsService {
    @Autowired
    PromotionsRepo promotionsRepo;
    @Override
    public Promotions create(PromotionsPayload model) {
        Promotions promotions = new Promotions();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        promotions.setName(model.getName());
        promotions.setDiscount(model.getDiscount());
        promotions.setStartDate(LocalDateTime.parse(model.getStartDate(), formatter));
        promotions.setEndDate(LocalDateTime.parse(model.getEndDate(), formatter));
        promotions.setIsDeleted(1);
        promotions.setStatus(1);
        promotions.setCreatedAt(now);
        promotions.setUpdatedAt(now);
        promotionsRepo.save(promotions);
        return promotions;
    }

    @Override
    public Page<Object> getAllAndSearch(PromotionsPayload filter) {
        org.springframework.data.domain.Page<Promotions> page = promotionsRepo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
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
    public Promotions findById(long id) {
        Optional<Promotions> promotions = promotionsRepo.findById(id);
        if(promotions.isEmpty()) {
            return null;
        }
        return promotions.get();
    }

    @Override
    public Promotions update(long id, PromotionsPayload payload) {
        Optional<Promotions> promotions = promotionsRepo.findById(id);
        if(promotions.isEmpty()) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        promotions.get().setName(payload.getName());
        promotions.get().setDiscount(payload.getDiscount());
        promotions.get().setStartDate(LocalDateTime.parse(payload.getStartDate(), formatter));
        promotions.get().setEndDate(LocalDateTime.parse(payload.getEndDate(), formatter));
        promotions.get().setUpdatedAt(now);
        promotionsRepo.save(promotions.get());
        return promotions.get();
    }

    @Override
    public boolean delete(long id) {
        Optional<Promotions> promotions = promotionsRepo.findById(id);
        if(promotions.isEmpty()) return false;
        promotions.get().setIsDeleted(0);
        promotionsRepo.save(promotions.get());
        return true;
    }

    @Override
    public boolean procedure(long id, PromotionsPayload promotionsPayload) {
        Optional<Promotions> promotions = promotionsRepo.findById(id);
        if(promotions.isEmpty()) {
            return false;
        }
        switch (promotionsPayload.getProcedure()) {
            case "hoạt động":
                promotions.get().setStatus(1);
                promotionsRepo.save(promotions.get());
                break;
            default:
                promotions.get().setStatus(0);
                promotionsRepo.save(promotions.get());
                break;
        }
        return true;
    }
}

