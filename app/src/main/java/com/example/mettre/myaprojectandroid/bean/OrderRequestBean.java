package com.example.mettre.myaprojectandroid.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单提交数据
 */
public class OrderRequestBean {

    private BigDecimal postage;//邮费
    private String buyerMessage;//买家留言
    private Long brandId;//品牌id
    private BigDecimal goodsTotal;//商品总价
    private BigDecimal userAllPrice;//订单总价（商品总价+邮费）
    private List<GoodsItem> goodsItems;
    private String recipientAddress;
    private String recipientName;
    private String recipientPhoneNumber;

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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
        private Long goodsId;
        private Long cartId;
        private int goodsNumber;//单个订单商品数量

        public Long getCartId() {
            return cartId;
        }

        public void setCartId(Long cartId) {
            this.cartId = cartId;
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
