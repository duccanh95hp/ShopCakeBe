package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.DeliveryAddress;
import com.example.shop_cake_be.payload.DeliveryAddressPayload;
import com.example.shop_cake_be.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/deliveryAddress")
public class DeliveryAddressController {
    @Autowired
    DeliveryAddressService service;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Result<?> create(@RequestBody DeliveryAddress address) {
        DeliveryAddress deliveryAddress = service.create(address);
        if(deliveryAddress == null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, deliveryAddress);
    }
    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody DeliveryAddressPayload filter) {
        Page<Object> categories = service.getAllAndSearch(filter);
        if (categories == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, categories);
        }
    }
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        Optional<DeliveryAddress> da = service.findById(id);
        if(da.isEmpty()) return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
        return Result.result(HttpStatus.OK.value(),SUCCESS,da);
    }
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") long id, @RequestBody DeliveryAddress model) {
        Optional<DeliveryAddress> cat = service.update(id, model);
        if(cat.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, cat);
    }
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean da = service.delete(id);
        if(da) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
    @PostMapping("/procedure/{id}")
    public Result<?> procedure(@PathVariable("id") long id, @RequestBody DeliveryAddressPayload payload) {
        boolean da = service.procedure(id, payload);
        if(da) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }

    @GetMapping("/findByUserId")
    public Result<?> findByUserId() {
        List<DeliveryAddress> das = service.findByUserId();
        if(das.isEmpty()) return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, das);
    }
}
