package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.common.Result;
import com.example.shop_cake_be.models.Comment;
import com.example.shop_cake_be.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService service;
    @PostMapping(value = "/create/{cakeId}")
    public Result<?> create(@RequestBody Comment comment, @PathVariable("cakeId") long cakeId) {
        Comment com = service.create(comment, cakeId);
        if(com == null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, com);
    }

    @PostMapping("/getAll")
    public Result<?> getAll() {
        List<Comment> comments = service.getAllAndSearch();
        if (comments == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, comments);
        }
    }
    @GetMapping("/findById/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        Optional<Comment> comment = service.findById(id);
        if (comment.isEmpty()) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,comment);
    }
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") long id) {
        boolean com = service.delete(id);
        if(com) return Result.result(HttpStatus.OK.value(), SUCCESS, null);
        return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND, null);
    }
    @GetMapping("/findByCakeId/{cakeId}")
    public Result<?> findByCakeId(@PathVariable("cakeId") long cakeId) {
        List<Comment> das = service.findByCakeId(cakeId);
        if(das.isEmpty()) return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, das);
    }
}

