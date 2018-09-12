package com.example.mettre.myaprojectandroid.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单列表
 */
public class OrderBean {

    private Long orderId;//订单id
    private Long orderNo;//订单编号
    private BigDecimal orderPrice;//订单总金额
    private BigDecimal payment;//总支付金额
    private BigDecimal postage;//邮费
    private Long brandId;//品牌id
    private String brandName;//品牌名称
    private Integer status = 10;//订单状态 0:已取消  10:未付款  20:已支付  30:已发货  40:交易成功  50:交易关闭
    private String recipientAddress;//收货地址
    private String recipientName;//收货姓名
    private String recipientPhoneNumber;//收货电话
    private String creationTime;
    private List<ListBean> orderItem;

    public static class ListBean {
        private Long goodsId;
        private String goodsName;
        private BigDecimal orderGoodsPrice;//订单商品价
        private String goodsDesc;//商品介绍
        private int quantity;
        private String orderItemId;

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

        public BigDecimal getOrderGoodsPrice() {
            return orderGoodsPrice;
        }

        public void setOrderGoodsPrice(BigDecimal orderGoodsPrice) {
            this.orderGoodsPrice = orderGoodsPrice;
        }

        public String getGoodsDesc() {
            return goodsDesc;
        }

        public void setGoodsDesc(String goodsDesc) {
            this.goodsDesc = goodsDesc;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderBean.ListBean> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderBean.ListBean> orderItem) {
        this.orderItem = orderItem;
    }

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

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
