package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.CakePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CakePromotionRepo extends JpaRepository<CakePromotion, Long> {

}
