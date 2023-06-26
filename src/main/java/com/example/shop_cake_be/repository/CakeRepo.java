package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.Cake;
import com.example.shop_cake_be.payload.CakePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CakeRepo extends JpaRepository<Cake,Long> {
    @Query("select ca from Cake ca " +
            "WHERE (:#{#filter.name} is null or lower(ca.name) like lower(concat('%', :#{#filter.name}, '%')) )" +
            "AND (:#{#filter.priceTo} is null or ca.price <= :#{#filter.priceTo})" +
            "AND (:#{#filter.priceFrom} is null or ca.price >= :#{#filter.priceFrom})" +
            "AND ca.isDeleted = 1 ORDER BY ca.id DESC")
    Page<Cake> getAllAndSearch(@Param("filter") CakePayload filter, Pageable pageable);
}