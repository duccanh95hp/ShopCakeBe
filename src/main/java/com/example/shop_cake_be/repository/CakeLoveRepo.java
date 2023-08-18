package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.CakeLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CakeLoveRepo extends JpaRepository<CakeLove, Long> {
    List<CakeLove> findByUserIdAndStatus(Long userId, int status);
    Optional<CakeLove> findByUserIdAndCakeId(Long userId, Long cakeId);
}
