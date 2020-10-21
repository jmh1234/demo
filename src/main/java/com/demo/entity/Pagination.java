package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库的分页结果
 */
@Getter
@Setter
public class Pagination<T> implements Serializable {
    private final List<T> items;
    private final int pageSize;
    private final int pageNum;
    private final int totalPage;

    private Pagination(List<T> items, int pageSize, int pageNum, int totalPage) {
        this.items = items;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalPage = totalPage;
    }

    public static <T> Pagination<T> pageOf(
            List<T> items, int pageSize, int pageNum, int totalPage) {
        return new Pagination<>(items, pageSize, pageNum, totalPage);
    }

    @Override
    public String toString() {
        return "Pagination{"
                + "items="
                + items
                + ", pageSize="
                + pageSize
                + ", pageNum="
                + pageNum
                + ", totalPage="
                + totalPage
                + '}';
    }
}
