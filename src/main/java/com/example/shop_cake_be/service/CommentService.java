package com.example.shop_cake_be.service;

import com.example.shop_cake_be.models.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment create(Comment model, long cakeId);
    List<Comment> getAllAndSearch();
    Optional<Comment> findById(long id);
    List<Comment> findByUserId();
    List<Comment> findByCakeId(long id);
    Optional<Comment> update(long id, Comment model, MultipartFile file);
    boolean delete(long id);
}
