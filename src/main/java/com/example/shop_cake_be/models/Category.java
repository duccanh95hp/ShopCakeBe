package com.example.shop_cake_be.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "status")
    private int status;
    @NotNull
    @Column(name = "description")
    private String description;
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
