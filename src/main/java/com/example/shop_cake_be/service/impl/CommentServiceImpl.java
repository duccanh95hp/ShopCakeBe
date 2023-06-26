package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.models.Comment;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.repository.CakeRepo;
import com.example.shop_cake_be.repository.CommentRepo;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepo repo;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CakeRepo cakeRepo;

    @Override
    public Comment create(Comment comment, long cakeId) {
        // lấy thông tin user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        // end
        Comment com = new Comment();
        com.setUserId(user.get().getId());
        com.setCakeId(cakeId);
        com.setContent(comment.getContent());
        com.setStatus(1);
        com.setIsDeleted(1);
        com.setCreatedAt(new Date());
        com.setUpdatedAt(new Date());
        repo.save(comment);
        return comment;
    }

    @Override
    public List<Comment> getAllAndSearch() {
        List<Comment> comments = repo.findAll();
        return comments;

    }

    @Override
    public Optional<Comment> findById(long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Comment> update(long id, Comment model, MultipartFile file) {
        Optional<Comment> comment = repo.findById(id);
        if(comment.isEmpty()) {
            return Optional.empty();
        }
        comment.get().setContent(model.getContent());
        comment.get().setUpdatedAt(new Date());
        repo.save(comment.get());
        return comment;
    }

    @Override
    public boolean delete(long id) {
        Optional<Comment> comment = repo.findById(id);
        if(comment.isEmpty()) {
            return false;
        }
        comment.get().setIsDeleted(0);
        repo.save(comment.get());
        return true;
    }

    @Override
    public List<Comment> findByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        List<Comment> das = repo.findByUserId(user.get().getId());
        if(das.isEmpty()) return null;
        return das;
    }

    @Override
    public List<Comment> findByCakeId(long id) {
        List<Comment> das = repo.findByCakeId(id);
        if(das.isEmpty()) return null;
        return das;
    }
}
