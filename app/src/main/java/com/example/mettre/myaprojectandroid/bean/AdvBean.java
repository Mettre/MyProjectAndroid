package com.example.mettre.myaprojectandroid.bean;

import java.util.Date;

/**
 * Created by Mettre on 2018/9/3.
 * 广告
 */
public class AdvBean {

    private Long adId;
    private Long adPositionId;
    private String adName;
    private String adDescribe;
    private int adType;//   "广告属性---1：跳转H5  2：跳转商品  3：公告")
    private String adLink;
    private Date startTime;
    private Date endTime;
    private String adImage;
    private boolean enabled = true;
    private int clickCount;

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getAdPositionId() {
        return adPositionId;
    }

    public void setAdPositionId(Long adPositionId) {
        this.adPositionId = adPositionId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdDescribe() {
        return adDescribe;
    }

    public void setAdDescribe(String adDescribe) {
        this.adDescribe = adDescribe;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
