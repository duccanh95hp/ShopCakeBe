package com.example.shop_cake_be.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Constants {
    public static final String NOT_FOUND = "Không tồn tại trong hệ thống";
    public static final String SUCCESS = "SUCCESS";
    public static final String EMPTY = "Không có dữ liệu";
    public static final String FAILURE = "thất bại";
    public static final String NEW_ORDER = "tạo mới";
    public static final String APPROVE = "xác nhận";
    public static final String TRANSPORT = "vận chuyển";
    public static final String COMPLETE = "hoàn thành";
    public static final String REJECT = "từ chối";
    public static final String ACTIVE = "hoạt động";
    public static final String IN_ACTIVE = "không hoạt động";
    public static final String WAIT_CONFIRM = "chờ xác nhận";
    public static final String CANCEL = "hủy";
}