package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.*;
import com.example.shop_cake_be.payload.CakePayload;
import com.example.shop_cake_be.repository.*;
import com.example.shop_cake_be.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@Service
@Transactional
public class CakeServiceImpl implements CakeService {
    @Autowired
    CakeRepo repo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    CakeCategoryRepo cakeCategoryRepo;

    @Autowired
    CakePromotionRepo cakePromotionRepo;
    @Autowired
    UserRepository userRepository;
    @Override
    public Cake create(Cake model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Cake cake = new Cake();

        cake.setName(model.getName());
        cake.setIngredient(model.getIngredient());
        cake.setPrice(model.getPrice());
        boolean hasRole = false;
        for (Role role : user.get().getRoles()) {
            if(String.valueOf(role.getName()).equals("ROLE_ADMIN")) {
                String name = String.valueOf(role.getName());
                hasRole = true;
                break;
            }
        }
        if(hasRole) {
            cake.setStatus(1);
        } else {
            cake.setStatus(3);
        }
        cake.setIsDeleted(1);
        cake.setCreatedAt(new Date());
        cake.setUpdatedAt(new Date());
        cake.setImage(model.getImage());
        cake.setColor(model.getColor());
        cake.setDecorate(model.getDecorate());
        cake.setTitle(model.getTitle());
        cake.setNote(model.getNote());
        cake.setSize(model.getSize());
        cake.setReason(model.getReason());
        cake.setUserId(user.get().getId());
        repo.save(cake);
        String[] parts = model.getListCategoryId().split(",");
        for (String part : parts) {
            Long categoryId = Long.parseLong(part);
            CakeCategory cakeCategory = new CakeCategory();
            cakeCategory.setCakeId(cake.getId());
            cakeCategory.setCategoryId(categoryId);
            cakeCategoryRepo.save(cakeCategory);
        }
        return cake;
    }

    @Override
    public Page<Object> getAllAndSearch(CakePayload filter) {
        org.springframework.data.domain.Page<Cake> page = repo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
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
    public Optional<Cake> findById(long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Cake> update(long id, Cake model) {
        Optional<Cake> cake = repo.findById(id);
        if(cake.isEmpty()) {
            return Optional.empty();
        }
        cake.get().setName(model.getName());
        cake.get().setImage(model.getImage());
        cake.get().setIngredient(model.getIngredient());
        cake.get().setUpdatedAt(new Date());
        cake.get().setColor(model.getColor());
        cake.get().setDecorate(model.getDecorate());
        cake.get().setTitle(model.getTitle());
        cake.get().setNote(model.getNote());
        cake.get().setSize(model.getSize());
        cake.get().setReason(model.getReason());
        repo.save(cake.get());
        List<CakeCategory> cakeCategories = cakeCategoryRepo.findByCakeId(id);
        for (CakeCategory cakeCategory : cakeCategories) {
            cakeCategoryRepo.deleteById(cakeCategory.getId());
        }
        String[] parts = model.getListCategoryId().split(",");
        for (String part : parts) {
            Long categoryId = Long.parseLong(part);
            CakeCategory cakeCategory = new CakeCategory();
            cakeCategory.setCakeId(id);
            cakeCategory.setCategoryId(categoryId);
            cakeCategoryRepo.save(cakeCategory);
        }
        return cake;
    }

    @Override
    public boolean delete(long id) {
        Optional<Cake> cake = repo.findById(id);
        if(cake.isEmpty()) {
            return false;
        }
        cake.get().setIsDeleted(0);
        repo.save(cake.get());
        return true;
    }

    @Override
    public boolean createPromotion(CakePromotion payload) {
        CakePromotion cakePromotion = new CakePromotion();
        cakePromotion.setCakeId(payload.getCakeId());
        cakePromotion.setPromotionId(payload.getPromotionId());
        cakePromotionRepo.save(cakePromotion);
        return true;
    }

    @Override
    public boolean procedure(long id, CakePayload cakePayload) {
        Optional<Cake> cake = repo.findById(id);
        if(!cake.isPresent()) {
            return false;
        }
        switch (cakePayload.getProcedure()) {
            case IN_ACTIVE:
                cake.get().setStatus(0);
                repo.save(cake.get());
                break;
            case ACTIVE:
                cake.get().setStatus(1);
                repo.save(cake.get());
                break;
            case WAIT_CONFIRM:
                cake.get().setStatus(2);
                repo.save(cake.get());
                break;
            case APPROVE:
                cake.get().setStatus(3);
                repo.save(cake.get());
                break;
            case COMPLETE:
                cake.get().setStatus(4);
                repo.save(cake.get());
                break;
            case REJECT:
                cake.get().setStatus(5);
                repo.save(cake.get());
                break;
            default:
                cake.get().setStatus(6);
                repo.save(cake.get());
                break;
        }
        return true;
    }
}
