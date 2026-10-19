package com.example.familyhealth.commom;

import java.util.List;

/**
 * 通用分页返回结构。
 * <p>
 * 分页没有引入 PageHelper 之类的插件，而是用最朴素的
 * {@code LIMIT offset, size} + {@code COUNT(*)} 两步实现：
 * 依赖少、SQL 一眼能看懂、也方便在面试里讲清楚原理。
 *
 * @param <T> 列表元素类型
 */
public class PageResult<T> {

    /** 总条数 */
    private long total;
    /** 当前页码，从 1 开始 */
    private int page;
    /** 每页条数 */
    private int size;
    /** 总页数 */
    private int pages;
    /** 当前页数据 */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(long total, int page, int size, List<T> records) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.records = records;
        this.pages = size <= 0 ? 0 : (int) ((total + size - 1) / size);
    }

    /** 页码兜底：非法值一律回落到第 1 页 */
    public static int safePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    /** 每页条数兜底：默认 10 条，上限 100 条，防止被刷成全表查询 */
    public static int safeSize(Integer size) {
        if (size == null || size < 1) {
            return 10;
        }
        return Math.min(size, 100);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
