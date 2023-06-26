package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "cake_promotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CakePromotion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "cake_id")
    private Long cakeId;
    @NotNull
    @Column(name = "promotion_id")
    private Long promotionId;
}
