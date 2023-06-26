package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.filter.OrderFilter;
import com.example.shop_cake_be.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("select o from Order o " +
            "WHERE ( :#{#filter.status} = 0 or o.status = :#{#filter.status} )" +
            "AND ( :#{#filter.userId} = 0L or o.userId = :#{#filter.userId})" +
            "AND ( :#{#filter.fromDate} is null or o.createdAt >=  :#{#filter.fromDate})" +
            "AND ( :#{#filter.toDate} is null or o.createdAt <=  :#{#filter.toDate})" +
            "ORDER BY o.id DESC")
    Page<Order> getAllAndSearch(@Param("filter") OrderFilter filter, Pageable pageable);
}
