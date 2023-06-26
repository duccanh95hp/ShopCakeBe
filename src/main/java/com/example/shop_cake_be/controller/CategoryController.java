package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.Category;
import com.example.shop_cake_be.payload.CategoryPayload;
import com.example.shop_cake_be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public Result<?> create(@RequestBody Category category) {
        Category cat = categoryService.create(category);
        return Result.result(HttpStatus.OK.value(), SUCCESS, cat);
    }
    @PostMapping("/getAll")
    public Result<?> getAll(@RequestBody CategoryPayload filter) {
        Page<Object> categories = categoryService.getAllAndSearch(filter);
        if (categories == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, categories);
        }
    }
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,category);
    }
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") long id, @RequestBody Category category) {
        Optional<Category> cat = categoryService.update(id, category);
        if(cat.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(), SUCCESS, cat);
    }
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean cat = categoryService.delete(id);
        if(cat) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
    @PostMapping("/procedure/{id}")
    public Result<?> procedure(@PathVariable("id") long id, @RequestBody CategoryPayload categoryPayload) {
        boolean cat = categoryService.procedure(id, categoryPayload);
        if(cat) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
}
