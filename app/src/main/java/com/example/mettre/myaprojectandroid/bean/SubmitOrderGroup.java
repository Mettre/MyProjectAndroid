package com.example.mettre.myaprojectandroid.bean;

import java.math.BigDecimal;
import java.util.List;

public class SubmitOrderGroup {

    private BigDecimal postage = new BigDecimal(0);//邮费
    private int brandId;//品牌id
    private String brandName;//品牌id
    private BigDecimal goodsTotal= new BigDecimal(0);//商品总价
    private BigDecimal userAllPrice= new BigDecimal(0);//订单总价（商品总价+邮费）
    private String buyerMessage;
    private List<GoodsItem> goodsItems;

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
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

    public BigDecimal getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(BigDecimal goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    public BigDecimal getUserAllPrice() {
        return userAllPrice;
    }

    public void setUserAllPrice(BigDecimal userAllPrice) {
        this.userAllPrice = userAllPrice;
    }

    public List<GoodsItem> getGoodsItems() {
        return goodsItems;
    }

    public void setGoodsItems(List<GoodsItem> goodsItems) {
        this.goodsItems = goodsItems;
    }

    public static class GoodsItem {
        private String goodsName;
        private BigDecimal goodsPrice= new BigDecimal(0);//商品价格
        private Long goodsId;
        private int goodsNumber;//单个订单商品数量

        public BigDecimal getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(BigDecimal goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public Long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Long goodsId) {
            this.goodsId = goodsId;
        }
        public int getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(int goodsNumber) {
            this.goodsNumber = goodsNumber;
        }
    }
}
