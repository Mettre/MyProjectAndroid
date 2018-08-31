package com.example.mettre.myaprojectandroid.bean;

/**
 * Created by Mettre on 2018/8/31.
 * 商品列表
 */

public class GoodsListBean {

    /**
     * goodsId : 3
     * goodsName : iphone7 64G 黑色
     * categoryId : 19
     * goodsSn : 2222222321221221
     * brandId : 20
     * stock : 99
     * marketPrice : 4999
     * shopPrice : 4688
     * goodsBrief : 苹果手机，西半球最好的手机
     * goodsDesc : iPhone7乔布斯的最后遗作，品质很好，质量很好
     * createTime : 2018-08-28 08:56:23
     * isPromote : false
     * lastUpdate : 2018-08-31 14:19:15
     */

    private int goodsId;
    private String goodsName;
    private int categoryId;
    private long goodsSn;
    private int brandId;
    private int stock;
    private int marketPrice;
    private int shopPrice;
    private String goodsBrief;
    private String goodsDesc;
    private String createTime;
    private boolean isPromote;
    private String lastUpdate;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public long getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(long goodsSn) {
        this.goodsSn = goodsSn;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(int shopPrice) {
        this.shopPrice = shopPrice;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}
