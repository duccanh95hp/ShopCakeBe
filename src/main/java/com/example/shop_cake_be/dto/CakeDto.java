package com.example.shop_cake_be.dto;

import com.example.shop_cake_be.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CakeDto {
    private Long id;
    private Long userId;
    private String name;
    private Double price;
    private String ingredient;
    private String title;
    private String decorate;
    private String note;
    private String size;
    private String color;
    private String reason;
    private String image;
    private int status;
    private int isDeleted;
    private Date createdAt;
    private Date updatedAt;
    private Double discount;
    private LocalDateTime promotionStartDate;
    private LocalDateTime promotionEndDate;
    private int special;
    private boolean isLike;
    List<Category> categoryList;
}
