package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cake_love")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CakeLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "cake_id")
    private Long cakeId;
    @Column(name = "status")
    private int status;
}
