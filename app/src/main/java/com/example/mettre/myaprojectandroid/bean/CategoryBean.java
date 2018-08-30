package com.example.mettre.myaprojectandroid.bean;

import java.util.List;

/**
 * 分类列表 大小分类集合
 */
public class CategoryBean {

    /**
     * 分类id
     */
    private int categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类名称
     */
    private String imgURL;

    /**
     * 父分类Id
     */
    private int parentId;

    /**
     * 分类列表
     */
    private List<CategoryBean> childCategory;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<CategoryBean> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<CategoryBean> childCategory) {
        this.childCategory = childCategory;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
