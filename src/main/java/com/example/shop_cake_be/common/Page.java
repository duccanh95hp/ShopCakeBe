package com.example.shop_cake_be.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int pageSize = 0;
    private int pageNumber = 0;
    private int totalPages = 0;
    private long totalItems = 0;
    private List<T> result;


    public static <T> Page<T> of(int pageSize, int pageNumber, int  totalPages, int totalItems, List<T> result) {
        Page<T> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNumber(pageNumber);
        page.setTotalPages(totalPages);
        page.setTotalItems(totalItems);
        page.setResult(result);
        return page;
    }
}
