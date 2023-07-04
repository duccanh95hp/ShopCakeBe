package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.ShoppingCartTmt;
import com.example.shop_cake_be.payload.ShoppingCartTmtPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartTmtRepo extends JpaRepository<ShoppingCartTmt,Long> {
    @Query("select sp from ShoppingCartTmt sp " +
            "WHERE (sp.userId = :#{#filter.userId}) " +
            "ORDER BY sp.id DESC")
    Page<ShoppingCartTmt> getAllAndSearch(@Param("filter") ShoppingCartTmtPayload filter, Pageable pageable);
    Optional<ShoppingCartTmt> findByUserIdAndCakeId(Long userId, Long cakeId);
}
