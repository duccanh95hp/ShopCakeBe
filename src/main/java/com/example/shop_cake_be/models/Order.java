package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_cake_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;
    @NotNull
    @Column(name = "order_code")
    private String orderCode;
    @NotNull
    @Column(name = "note")
    private String note;
    @NotNull
    @Column(name = "reason")
    private String reason;
    @NotNull
    @Column(name = "status")
    private int status;
    @NotNull
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
