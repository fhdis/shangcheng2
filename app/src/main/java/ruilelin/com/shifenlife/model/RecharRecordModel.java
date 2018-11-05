package ruilelin.com.shifenlife.model;

import java.util.Date;

public class RecharRecordModel {
    private int id;
    private int userId;
    private int money;
    private Date createTime;
    private Date paidTime;
    private String type;
    private String payWay;
    private String note;
    private String status;

    public RecharRecordModel() {
    }

    public RecharRecordModel(int id, int userId, int money, Date createTime, Date paidTime, String type, String payWay, String note, String status) {
        this.id = id;
        this.userId = userId;
        this.money = money;
        this.createTime = createTime;
        this.paidTime = paidTime;
        this.type = type;
        this.payWay = payWay;
        this.note = note;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
