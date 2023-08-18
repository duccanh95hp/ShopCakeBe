package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.dto.CakeDto;
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
    PromotionsRepo promotionsRepo;
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
        cake.setPromotionId(0l);
        cake.setSpecial(model.getSpecial());
        cake.setLike(false);
        repo.save(cake);
        if(!model.getListCategoryId().equals("")) {
            String[] parts = model.getListCategoryId().split(",");
            for (String part : parts) {
                Long categoryId = Long.parseLong(part);
                CakeCategory cakeCategory = new CakeCategory();
                cakeCategory.setCakeId(cake.getId());
                cakeCategory.setCategoryId(categoryId);
                cakeCategoryRepo.save(cakeCategory);
            }
        }
        return cake;
    }

    @Override
    public Page<Object> getAllAndSearch(CakePayload filter) {

        org.springframework.data.domain.Page<Cake> page = repo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>();
        for (Cake c : page.getContent()) {
            CakeDto cakeDto = new CakeDto();
            cakeDto.setId(c.getId());
            cakeDto.setUserId(c.getUserId());
            cakeDto.setName(c.getName());
            cakeDto.setPrice(c.getPrice());
            cakeDto.setIngredient(c.getIngredient());
            cakeDto.setTitle(c.getTitle());
            cakeDto.setDecorate(c.getDecorate());
            cakeDto.setNote(c.getNote());
            cakeDto.setSize(c.getSize());
            cakeDto.setColor(c.getColor());
            cakeDto.setReason(c.getReason());
            cakeDto.setImage(c.getImage());
            cakeDto.setStatus(c.getStatus());
            cakeDto.setIsDeleted(c.getIsDeleted());
            cakeDto.setSpecial(c.getSpecial());
            cakeDto.setCreatedAt(c.getCreatedAt());
            cakeDto.setUpdatedAt(c.getUpdatedAt());
            cakeDto.setLike(c.isLike());
            Optional<Promotions> promotions = promotionsRepo.findById(c.getPromotionId());
            if(promotions.isPresent()) {
                cakeDto.setDiscount(promotions.get().getDiscount());
                cakeDto.setPromotionStartDate(promotions.get().getStartDate());
                cakeDto.setPromotionEndDate(promotions.get().getEndDate());
            }
            List<CakeCategory> cakeCategories = cakeCategoryRepo.findByCakeId(c.getId());
            List<Category> categories = new ArrayList<>();
            for (CakeCategory cakeCategory : cakeCategories) {
                Category category = categoryRepo.findById(cakeCategory.getCategoryId()).get();
                categories.add(category);
            }
            cakeDto.setCategoryList(categories);
            objectList.add(cakeDto);
        }
        return com.example.shop_cake_be.common.Page.builder()
                .result(objectList)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(filter.getSize())
                .pageNumber(filter.getPage())
                .build();
    }

    @Override
    public CakeDto findById(long id) {
        Optional<Cake> c = repo.findById(id);
        if(c.isPresent()){
            CakeDto cakeDto = new CakeDto();
            cakeDto.setId(c.get().getId());
            cakeDto.setUserId(c.get().getUserId());
            cakeDto.setName(c.get().getName());
            cakeDto.setPrice(c.get().getPrice());
            cakeDto.setIngredient(c.get().getIngredient());
            cakeDto.setTitle(c.get().getTitle());
            cakeDto.setDecorate(c.get().getDecorate());
            cakeDto.setNote(c.get().getNote());
            cakeDto.setSize(c.get().getSize());
            cakeDto.setColor(c.get().getColor());
            cakeDto.setReason(c.get().getReason());
            cakeDto.setImage(c.get().getImage());
            cakeDto.setStatus(c.get().getStatus());
            cakeDto.setIsDeleted(c.get().getIsDeleted());
            cakeDto.setSpecial(c.get().getSpecial());
            cakeDto.setCreatedAt(c.get().getCreatedAt());
            cakeDto.setUpdatedAt(c.get().getUpdatedAt());
            cakeDto.setLike(c.get().isLike());
            Optional<Promotions> promotions = promotionsRepo.findById(c.get().getPromotionId());
            if(promotions.isPresent()) {
                cakeDto.setDiscount(promotions.get().getDiscount());
                cakeDto.setPromotionStartDate(promotions.get().getStartDate());
                cakeDto.setPromotionEndDate(promotions.get().getEndDate());
            }
            return cakeDto;
        }
        return null;
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
        cake.get().setSpecial(model.getSpecial());
        cake.get().setPrice(model.getPrice());
        cake.get().setListCategoryId(model.getListCategoryId());
        repo.save(cake.get());
        List<CakeCategory> cakeCategories = cakeCategoryRepo.findByCakeId(id);
        for (CakeCategory cakeCategory : cakeCategories) {
            cakeCategoryRepo.deleteById(cakeCategory.getId());
        }
        if(!model.getListCategoryId().equals("")) {
            String[] parts = model.getListCategoryId().split(",");

            for (String part : parts) {
                Long categoryId = Long.parseLong(part);
                CakeCategory cakeCategory = new CakeCategory();
                cakeCategory.setCakeId(id);
                cakeCategory.setCategoryId(categoryId);
                cakeCategoryRepo.save(cakeCategory);

            }
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

    @Override
    public List<Cake> getCakeByPromotion(Long promotionId) {
        List<Cake> cakes = repo.findByPromotionIdAndIsDeleted(promotionId, 1);
        return cakes;
    }

    @Override
    public List<CakeDto> findByCategory(Long categoryId) {
        List<CakeCategory> cakeCategories = cakeCategoryRepo.findByCategoryId(categoryId);
        List<CakeDto> cakes = new ArrayList<>();
        for (CakeCategory cakeCategory : cakeCategories) {
            Cake c = repo.findByIdAndIsDeleted(cakeCategory.getCakeId(), 1);
            if(c != null) {
                CakeDto cakeDto = new CakeDto();
                cakeDto.setId(c.getId());
                cakeDto.setUserId(c.getUserId());
                cakeDto.setName(c.getName());
                cakeDto.setPrice(c.getPrice());
                cakeDto.setIngredient(c.getIngredient());
                cakeDto.setTitle(c.getTitle());
                cakeDto.setDecorate(c.getDecorate());
                cakeDto.setNote(c.getNote());
                cakeDto.setSize(c.getSize());
                cakeDto.setColor(c.getColor());
                cakeDto.setReason(c.getReason());
                cakeDto.setImage(c.getImage());
                cakeDto.setStatus(c.getStatus());
                cakeDto.setIsDeleted(c.getIsDeleted());
                cakeDto.setSpecial(c.getSpecial());
                cakeDto.setCreatedAt(c.getCreatedAt());
                cakeDto.setUpdatedAt(c.getUpdatedAt());
                cakeDto.setLike(c.isLike());
                Optional<Promotions> promotions = promotionsRepo.findById(c.getPromotionId());
                if(promotions.isPresent()) {
                    cakeDto.setDiscount(promotions.get().getDiscount());
                    cakeDto.setPromotionStartDate(promotions.get().getStartDate());
                    cakeDto.setPromotionEndDate(promotions.get().getEndDate());
                }
                cakes.add(cakeDto);
            }
        }
        return cakes;
    }

    @Override
    public List<CakeDto> getBySpecial() {
        List<Cake> cakes = repo.findBySpecialAndIsDeleted(1,1);
        List<CakeDto> cakeDtoList = new ArrayList<>();
        if(!cakes.isEmpty()) {
            for (Cake c : cakes) {
                CakeDto cakeDto = new CakeDto();
                cakeDto.setId(c.getId());
                cakeDto.setUserId(c.getUserId());
                cakeDto.setName(c.getName());
                cakeDto.setPrice(c.getPrice());
                cakeDto.setIngredient(c.getIngredient());
                cakeDto.setTitle(c.getTitle());
                cakeDto.setDecorate(c.getDecorate());
                cakeDto.setNote(c.getNote());
                cakeDto.setSize(c.getSize());
                cakeDto.setColor(c.getColor());
                cakeDto.setReason(c.getReason());
                cakeDto.setImage(c.getImage());
                cakeDto.setStatus(c.getStatus());
                cakeDto.setIsDeleted(c.getIsDeleted());
                cakeDto.setSpecial(c.getSpecial());
                cakeDto.setCreatedAt(c.getCreatedAt());
                cakeDto.setUpdatedAt(c.getUpdatedAt());
                cakeDto.setLike(c.isLike());
                Optional<Promotions> promotions = promotionsRepo.findById(c.getPromotionId());
                if(promotions.isPresent()) {
                    cakeDto.setDiscount(promotions.get().getDiscount());
                    cakeDto.setPromotionStartDate(promotions.get().getStartDate());
                    cakeDto.setPromotionEndDate(promotions.get().getEndDate());
                }
                cakeDtoList.add(cakeDto);
            }
            return cakeDtoList;
        }
        return null;
    }
}
