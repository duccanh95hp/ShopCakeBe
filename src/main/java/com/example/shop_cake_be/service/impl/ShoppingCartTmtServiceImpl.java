package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.dto.ShoppingCartTmtDto;
import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.models.ShoppingCartTmt;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.payload.ShoppingCartTmtPayload;
import com.example.shop_cake_be.repository.CakeRepo;
import com.example.shop_cake_be.repository.ShoppingCartTmtRepo;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.ShoppingCartTmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ShoppingCartTmtServiceImpl implements ShoppingCartTmtService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShoppingCartTmtRepo shoppingCartTmtRepo;
    @Autowired
    CakeRepo cakeRepo;
    @Override
    public ShoppingCartTmt create(ShoppingCartTmt model) {
        User user = this.user();
        Optional<Cake> cake = cakeRepo.findById(model.getCakeId());
        if(cake.isEmpty()) {
            return null;
        }
        Optional<ShoppingCartTmt> shoppingCartTmt = shoppingCartTmtRepo.findByUserIdAndCakeId(user.getId(), model.getCakeId());
        if(shoppingCartTmt.isEmpty()) {
            ShoppingCartTmt tmt = new ShoppingCartTmt();
            tmt.setUserId(user().getId());
            tmt.setCakeId(model.getCakeId());
            tmt.setQuantity(model.getQuantity());
            shoppingCartTmtRepo.save(tmt);
            return tmt;
        } else {
            shoppingCartTmt.get().setQuantity(shoppingCartTmt.get().getQuantity() + model.getQuantity());
            shoppingCartTmtRepo.save(shoppingCartTmt.get());
            return shoppingCartTmt.get();
        }
    }

    @Override
    public Page<Object> getAllAndSearch(ShoppingCartTmtPayload filter) {
        User user = this.user();
        filter.setUserId(user.getId());
        org.springframework.data.domain.Page<ShoppingCartTmt> page = shoppingCartTmtRepo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>();
        for (ShoppingCartTmt tmt : page) {
            ShoppingCartTmtDto dto = new ShoppingCartTmtDto();
            Optional<Cake> cake = cakeRepo.findById(tmt.getCakeId());
            if(cake.isEmpty()) {
                throw new RuntimeException("Cake not found");
            }
            dto.setShoppingCartTmtId(tmt.getId());
            dto.setCakeId(tmt.getCakeId());
            dto.setNameCake(cake.get().getName());
            dto.setPriceCake(cake.get().getPrice());
            dto.setQuantityShoppingCartTmt(tmt.getQuantity());
            dto.setImageCake(cake.get().getImage());
            objectList.add(dto);
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
    public ShoppingCartTmt update(long id, ShoppingCartTmt model) {
        Optional<ShoppingCartTmt> shoppingCartTmt = shoppingCartTmtRepo.findById(id);
        if(shoppingCartTmt.isEmpty()) {
            return null;
        }
        shoppingCartTmt.get().setQuantity(model.getQuantity());
        shoppingCartTmtRepo.save(shoppingCartTmt.get());
        return shoppingCartTmt.get();
    }

    @Override
    public boolean delete(long id) {
        Optional<ShoppingCartTmt> shoppingCartTmt = shoppingCartTmtRepo.findById(id);
        if(shoppingCartTmt.isEmpty()) {
            return false;
        }
        shoppingCartTmtRepo.delete(shoppingCartTmt.get());
        return true;
    }
    public User user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        if (!users.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return users.get();
    }
}
