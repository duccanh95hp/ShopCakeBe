package com.example.shop_cake_be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserPayload {
    private Long id;
    private String username;
    private String email;
    private String birthday;
    private String telephone;
    private String fullName;
    private String address;
}
