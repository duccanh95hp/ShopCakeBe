package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.CakeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CakeCategoryRepo extends JpaRepository<CakeCategory, Long> {
    @Override
    List<CakeCategory> findAll();
    List<CakeCategory> findByCategoryId(long categoryId);
    List<CakeCategory> findByCakeId(long cakeId);
}
