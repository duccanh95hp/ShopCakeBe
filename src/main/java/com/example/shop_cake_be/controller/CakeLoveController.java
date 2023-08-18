package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.dto.CakeDto;
import com.example.shop_cake_be.service.CakeLoveService;
import com.example.shop_cake_be.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.shop_cake_be.common.Constants.FAILURE;
import static com.example.shop_cake_be.common.Constants.SUCCESS;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cakeLove")
public class CakeLoveController {
    @Autowired
    CakeLoveService cakeLoveService;

    @PostMapping("/create")
    public Result<?> create(@RequestParam Long cakeId) {
        boolean result = cakeLoveService.create(cakeId);
        if (result) return Result.success();
        return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
    }
    @GetMapping("getByUser")
    public Result<?> getByUser(){
        List<CakeDto> cakeDtoList = cakeLoveService.getCakeLoveByUser();
        return Result.result(HttpStatus.OK.value(), SUCCESS, cakeDtoList);
    }
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam Long cakeId) {
        boolean result = cakeLoveService.delete(cakeId);
        if (result) return Result.success();
        return Result.result(HttpStatus.BAD_REQUEST.value(),FAILURE, null);
    }
}
