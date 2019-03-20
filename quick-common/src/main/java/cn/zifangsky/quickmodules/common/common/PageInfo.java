package cn.zifangsky.quickmodules.common.common;

import cn.zifangsky.quickmodules.common.enums.SortOrderTypes;

/**
 * 分页参数
 *
 * @author zifangsky
 * @date 2017/11/14
 * @since 1.0.0
 */
public class PageInfo {
    /**
     * 数据总数
     */
    private Long count;
    /**
     * 页数
     */
    private Long pageCount;
    /**
     * 每页展示数据量
     */
    private Integer pageSize = 10;
    /**
     * 当前页
     */
    private Integer currentPage = 1;
    /**
     * 按照什么字段排序
     */
    private String sortName = "id";
    /**
     * ASC（正序）、DESC（倒序）
     */
    private String sortOrder = SortOrderTypes.ASC.getCode();

    public PageInfo() {
    }

    public PageInfo(Integer pageSize, Integer currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PageInfo(Integer pageSize, Integer currentPage, String sortName) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.sortName = sortName;
    }

    public PageInfo(Integer pageSize, Integer currentPage, String sortName, String sortOrder) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "count=" + count +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", sortName='" + sortName + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
