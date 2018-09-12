package com.example.mettre.myaprojectandroid.bean;

import java.math.BigDecimal;

public class GoodsDetailsBean {


    /**
     * goodsId : 2
     * goodsName : 华为P20黑色32G
     * categoryId : 35
     * categoryName : 智能手机
     * goodsSn : 233111113423423420
     * brandId : 42
     * brandName : 华为
     * stock : 999
     * marketPrice : 4688
     * shopPrice : 4599
     * goodsBrief : 华为最好用的手机
     * goodsDesc : 华为最好用的手机
     * isPromote : false
     */

    private Long goodsId;
    private String goodsName;
    private int categoryId;
    private String categoryName;
    private long goodsSn;
    private Long brandId;
    private String brandName;
    private int stock;
    private BigDecimal marketPrice;
    private BigDecimal shopPrice;
    private String goodsBrief;
    private String goodsDesc;
    private boolean isPromote;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

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

    public long getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(long goodsSn) {
        this.goodsSn = goodsSn;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(BigDecimal shopPrice) {
        this.shopPrice = shopPrice;
    }

    public boolean isPromote() {
        return isPromote;
    }

    public void setPromote(boolean promote) {
        isPromote = promote;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public boolean isIsPromote() {
        return isPromote;
    }

    public void setIsPromote(boolean isPromote) {
        this.isPromote = isPromote;
    }
}
