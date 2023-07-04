package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "shopping_cart_tmt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartTmt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "cake_id")
    private Long cakeId;
    @Column(name = "quantity")
    private Integer quantity;
}
