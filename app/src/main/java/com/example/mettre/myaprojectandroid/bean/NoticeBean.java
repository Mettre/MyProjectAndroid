package com.example.mettre.myaprojectandroid.bean;

/**
 * 公告
 */
public class NoticeBean {
    /**
     * creationTime : 1536754983751
     * noticeDescribe : 新年快乐新年快乐新年快乐新年快乐新年快乐
     * noticeId : 49743066751307780
     * noticeLink : https://www.baidu.com
     * noticeName : 新年快乐
     * readCount : 0
     */

    private long creationTime;
    private String noticeDescribe;
    private long noticeId;
    private String noticeLink;
    private String noticeName;
    private int readCount;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getNoticeDescribe() {
        return noticeDescribe;
    }

    public void setNoticeDescribe(String noticeDescribe) {
        this.noticeDescribe = noticeDescribe;
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeLink() {
        return noticeLink;
    }

    public void setNoticeLink(String noticeLink) {
        this.noticeLink = noticeLink;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
