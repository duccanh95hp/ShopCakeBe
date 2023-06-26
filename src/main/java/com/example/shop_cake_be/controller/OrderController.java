package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.dto.OrderDetailDto;
import com.example.shop_cake_be.models.Order;
import com.example.shop_cake_be.payload.OrderPayload;
import com.example.shop_cake_be.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @PostMapping("/create")
    public Result<?> create(@RequestBody OrderPayload model) {
        Order order = orderService.create(model);
        if(order == null) {
            return Result.result(HttpStatus.BAD_REQUEST.value(), "Tạo đơn hàng thất bại", null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS , order);
    }
    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody OrderPayload filter) {
        Page<Object> orders = orderService.getAllAndSearch(filter);
        if (orders == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, orders);
        }
    }
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        List<OrderDetailDto> orderDto = orderService.detailOrder(id);
        if (orderDto == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,orderDto);
    }
    @PostMapping("/procedure/{id}")
    public Result<?> procedure(@PathVariable("id") long id, @RequestBody OrderPayload payload) {
        boolean order = orderService.procedure(id, payload);
        if(order) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
}
