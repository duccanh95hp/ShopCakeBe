package com.example.shop_cake_be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentPayload {
    private String content;
    private String status;
    private Date create_at;
    private int size;
    private int page;
    private String procedure;
}