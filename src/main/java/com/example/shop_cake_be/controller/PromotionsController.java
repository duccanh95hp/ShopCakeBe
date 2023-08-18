package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.Promotions;
import com.example.shop_cake_be.payload.PromotionAddOrDelPayLoad;
import com.example.shop_cake_be.payload.PromotionsPayload;
import com.example.shop_cake_be.service.PromotionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/promotions")
public class PromotionsController {
    @Autowired
    PromotionsService promotionsService;
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Result<?> create(@RequestBody PromotionsPayload payload) {
        Promotions promotions = promotionsService.create(payload);
        return Result.result(HttpStatus.OK.value(), SUCCESS, promotions);
    }

    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody PromotionsPayload filter) {
        Page<Object> promotionsList = promotionsService.getAllAndSearch(filter);
        if (promotionsList == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, promotionsList);
        }
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        Promotions promotions = promotionsService.findById(id);
        if (promotions == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,promotions);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") long id, @RequestBody PromotionsPayload payload) {
        Promotions promotions = promotionsService.update(id, payload);
        if(promotions == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, promotions);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean promotion = promotionsService.delete(id);
        if(promotion) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/procedure/{id}")
    public Result<?> procedure(@PathVariable("id") long id, @RequestBody PromotionsPayload payload) {
        boolean promotion = promotionsService.procedure(id, payload);
        if(promotion) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addOrDeleteCake")
    public Result<?> addOrDeleteCake(@RequestBody PromotionAddOrDelPayLoad payLoad) {
        boolean result = promotionsService.promotionAddOrDelCake(payLoad);
        if(result) return Result.result(HttpStatus.OK.value(), SUCCESS, result);
        return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
    }
}

