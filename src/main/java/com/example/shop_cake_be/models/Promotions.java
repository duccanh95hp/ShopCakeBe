package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotions implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "discount")
    private Double discount;
    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @NotNull
    @Column(name = "is_deleted")
    private int isDeleted;
    @NotNull
    @Column(name = "status")
    private int status;
    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

