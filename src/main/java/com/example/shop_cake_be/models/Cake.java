package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cake")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cake implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "price")
    private Double price;
    @NotNull
    @Column(name = "ingredient")
    private String ingredient;
    @Column(name = "title")
    private String title;
    @Column(name = "decorate")
    private String decorate;
    @Column(name = "note")
    private String note;
    @Column(name = "size")
    private String size;
    @Column(name = "color")
    private String color;
    @Column(name = "reason")
    private String reason;
    @NotNull
    @Column(name = "image")
    private String image;
    @NotNull
    @Column(name = "status")
    private int status;
    @NotNull
    @Column(name = "is_deleted")
    private int isDeleted;
    @NotNull
    @Column(name = "created_at")
    private Date createdAt;
    @NotNull
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "promotion_id")
    private Long promotionId;
    @Column(name = "special")
    private int special;
    @Column(name = "is_like")
    private boolean isLike;

    @Transient
    private String listCategoryId;

}
