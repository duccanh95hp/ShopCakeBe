package com.example.shop_cake_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "cake_id")
    private Long cakeId;
    @NotNull
    @Column(name = "content")
    private String content;
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
}

