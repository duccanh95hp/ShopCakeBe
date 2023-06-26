package com.example.shop_cake_be.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private T data;

    public static Result success(Object data) {
        return Result.result(ResultCode.SUCCESS, "success", data);
    }
    public static Result result(Integer code, String message, Object data) {
        Result result = new Result();
        return result.setCode(code)
                .setMessage(message)
                .setData(data);
    }
    public static Result success() {
        return Result.success(null);
    }
}
