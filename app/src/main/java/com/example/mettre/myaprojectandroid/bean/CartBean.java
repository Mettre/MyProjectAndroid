package com.example.mettre.myaprojectandroid.bean;

import java.math.BigDecimal;
import java.util.List;

public class CartBean {

    private int brandId;//品牌id
    private String brandName;//品牌名
    private List<CartGoodsItem> goodsItem;
    protected boolean selected;
    private BigDecimal selectPrice;//选中商品总价格

    public BigDecimal getSelectPrice() {
        return selectPrice;
    }

    public void setSelectPrice(BigDecimal selectPrice) {
        this.selectPrice = selectPrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<CartGoodsItem> getGoodsItem() {
        return goodsItem;
    }

    public void setGoodsItem(List<CartGoodsItem> goodsItem) {
        this.goodsItem = goodsItem;
    }
}
