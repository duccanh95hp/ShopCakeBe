package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "delivery_address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "user_id")
    private long userId;
    @NotNull
    @Column(name = "address")
    private String address;
    @NotNull
    @Column(name = "phone")
    private String phone;
    @NotNull
    @Column(name = "status")
    private int status;
    @NotNull
    @Column(name = "is_deleted")
    private int isDeleted;
    @Column(name = "name")
    private String name;
}

