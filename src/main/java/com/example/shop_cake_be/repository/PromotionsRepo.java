package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.Promotions;
import com.example.shop_cake_be.payload.PromotionsPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionsRepo extends JpaRepository<Promotions, Long> {
    @Query("select p from Promotions p " +
            "WHERE ( :#{#filter.name} is null or lower(p.name) like lower(concat('%', :#{#filter.name}, '%')) )" +
            "AND p.isDeleted = 1 ORDER BY p.id DESC")
    Page<Promotions> getAllAndSearch(@Param("filter") PromotionsPayload filter, Pageable pageable);
}
