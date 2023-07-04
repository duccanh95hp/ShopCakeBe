package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.models.ShoppingCartTmt;
import com.example.shop_cake_be.payload.CakePayload;
import com.example.shop_cake_be.payload.ShoppingCartTmtPayload;
import com.example.shop_cake_be.service.ShoppingCartTmtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;
import static com.example.shop_cake_be.common.Constants.NOT_FOUND;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/shoppingCartTmt")
public class ShoppingCartTmtController {
    @Autowired
    ShoppingCartTmtService shoppingCartTmtService;

    @PostMapping("/create")
    public Result<?> create(@RequestBody ShoppingCartTmt payload) {
        ShoppingCartTmt shoppingCartTmt = shoppingCartTmtService.create(payload);
        if(shoppingCartTmt == null) {
            return Result.result(HttpStatus.BAD_REQUEST.value(), "Cake not found", null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, shoppingCartTmt);
    }
    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody ShoppingCartTmtPayload filter) {
        Page<Object> page = shoppingCartTmtService.getAllAndSearch(filter);
        if (page == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, page);
        }
    }
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") long id, @RequestBody ShoppingCartTmt model) throws JsonProcessingException {

        ShoppingCartTmt shoppingCartTmt = shoppingCartTmtService.update(id, model);
        if(shoppingCartTmt == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, shoppingCartTmt);
    }
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean shoppingCartTmt = shoppingCartTmtService.delete(id);
        if(shoppingCartTmt) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
}
