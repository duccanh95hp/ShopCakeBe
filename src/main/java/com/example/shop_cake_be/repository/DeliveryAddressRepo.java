package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.DeliveryAddress;
import com.example.shop_cake_be.payload.DeliveryAddressPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAddressRepo extends JpaRepository<DeliveryAddress, Long> {
    @Query("select da from DeliveryAddress da " +
            "WHERE ( :#{#filter.address} is null or lower(da.address) like lower(concat('%', :#{#filter.address}, '%')) )" +
            "AND da.userId = :#{#filter.userId} " +
            "AND da.isDeleted = 1 ORDER BY da.id DESC")
    Page<DeliveryAddress> getAllAndSearch(@Param("filter") DeliveryAddressPayload filter, Pageable pageable);
    List<DeliveryAddress> findByUserId(long id);

    List<DeliveryAddress> findByUserIdAndStatus(long id,int status);
}
