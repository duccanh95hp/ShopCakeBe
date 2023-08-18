package com.example.shop_cake_be.payload;

import com.example.shop_cake_be.models.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderPayload {
    private Long userId;
    private int status;
    private String fromDate;
    private String toDate;
    private int size;
    private int page;
    private String procedure;
    private Long deliveryAddressId;
    private String note;

    private String reason;

    private String deliveryDate;

    private List<OrderDetail> orderDetails;
    private int limit;
}
