package com.example.mettre.myaprojectandroid.bean;

import java.io.Serializable;

/**
 * Created by Mettre on 2018/8/31.
 * 商品列表
 */

public class GoodsListBean implements Serializable{


    /**
     * brandId : 42
     * brandName : 华为
     * categoryId : 35
     * categoryName : 智能手机
     * createTime : 2018-09-01 19:49:19
     * goodsBrief : 华为最好用的手机
     * goodsDesc : 华为最好用的手机
     * goodsId : 2
     * goodsName : 华为P20黑色32G
     * goodsSn : 233111113423423432
     * isPromote : false
     * lastUpdate : 2018-09-01 19:49:19
     * marketPrice : 4688.0
     * shopPrice : 4599.0
     * stock : 999
     */

    private int brandId;
    private String brandName;
    private int categoryId;
    private String categoryName;
    private String createTime;
    private String goodsBrief;
    private String goodsDesc;
    private int goodsId;
    private String goodsName;
    private long goodsSn;
    private boolean isPromote;
    private String lastUpdate;
    private double marketPrice;
    private double shopPrice;
    private int stock;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(long goodsSn) {
        this.goodsSn = goodsSn;
    }

    public boolean isIsPromote() {
        return isPromote;
    }

    public void setIsPromote(boolean isPromote) {
        this.isPromote = isPromote;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
