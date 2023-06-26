package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "order_id")
    private Long orderId;
    @NotNull
    @Column(name = "cake_id")
    private Long cakeId;
    @NotNull
    @Column(name = "price")
    private Double price;
    @NotNull
    @Column(name = "quantity")
    private int quantity;

}