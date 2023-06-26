package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.Category;
import com.example.shop_cake_be.payload.CategoryPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("select ca from Category ca " +
            "WHERE ( :#{#filter.name} is null or lower(ca.name) like lower(concat('%', :#{#filter.name}, '%')) )" +
            "AND ca.isDeleted = 1 ORDER BY ca.id DESC")
    Page<Category> getAllAndSearch(@Param("filter") CategoryPayload filter, Pageable pageable);
}
