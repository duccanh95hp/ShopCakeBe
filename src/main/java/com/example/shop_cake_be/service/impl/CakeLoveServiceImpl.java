package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.dto.CakeDto;
import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.models.CakeLove;
import com.example.shop_cake_be.models.Promotions;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.repository.CakeLoveRepo;
import com.example.shop_cake_be.repository.CakeRepo;
import com.example.shop_cake_be.repository.PromotionsRepo;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.CakeLoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CakeLoveServiceImpl implements CakeLoveService {
    @Autowired
    CakeLoveRepo cakeLoveRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CakeRepo cakeRepo;
    @Autowired
    PromotionsRepo promotionsRepo;
    @Transactional
    @Override
    public boolean create(Long cakeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<CakeLove> cakeLove = cakeLoveRepo.findByUserIdAndCakeId(user.get().getId(), cakeId);
        if(cakeLove.isPresent()) {
            Cake cake = cakeRepo.findByIdAndIsDeleted(cakeId, 1);
            cake.setLike(true);
            cakeRepo.save(cake);
            cakeLove.get().setStatus(1);
            cakeLoveRepo.save(cakeLove.get());
        } else {
            CakeLove cakeLove1 = new CakeLove();
            cakeLove1.setStatus(1);
            cakeLove1.setCakeId(cakeId);
            cakeLove1.setUserId(user.get().getId());
            cakeLoveRepo.save(cakeLove1);
        }

        return true;
    }

    @Override
    public List<CakeDto> getCakeLoveByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        List<CakeLove> cakeLoves = cakeLoveRepo.findByUserIdAndStatus(user.get().getId(), 1);
        List<CakeDto> cakeDtoList = new ArrayList<>();
        for (CakeLove cakeLove : cakeLoves) {
            Optional<Cake> c = cakeRepo.findById(cakeLove.getCakeId());
            if(c.isPresent()) {
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
                if (promotions.isPresent()) {
                    cakeDto.setDiscount(promotions.get().getDiscount());
                    cakeDto.setPromotionStartDate(promotions.get().getStartDate());
                    cakeDto.setPromotionEndDate(promotions.get().getEndDate());
                }
                cakeDtoList.add(cakeDto);
            }
        }
        return cakeDtoList;
    }

    @Override
    public boolean delete(Long cakeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<CakeLove> cakeLove = cakeLoveRepo.findByUserIdAndCakeId(user.get().getId(), cakeId);
        if(cakeLove.isPresent()) {
            cakeLove.get().setStatus(0);
            cakeLoveRepo.save(cakeLove.get());
            return true;
        }
        return false;
    }
}
