package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @PutMapping
    public Result test() {
        return Result.result(HttpStatus.OK.value(), "test", null);
    }
}
