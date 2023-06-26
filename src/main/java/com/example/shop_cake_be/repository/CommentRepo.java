package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {


    List<Comment> findByUserId(long id);
    List<Comment> findByCakeId(long id);
}
