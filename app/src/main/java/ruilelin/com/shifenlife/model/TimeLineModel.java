package ruilelin.com.shifenlife.model;

import java.math.BigDecimal;
import java.util.Date;

public class TimeLineModel {
    private BigDecimal money;//消费金额
    private String sn;//订单号
    private Date createTime;//用户流水记录创建时间
    private BigDecimal userBalance;//用户实时余额
    private String shopname;//店铺名称
    private String type;//交易类型(IN 收入 OUT 支出)
    private String note;//备注

    public TimeLineModel() {

    }


    public TimeLineModel(BigDecimal money, String sn, Date createTime, BigDecimal userBalance, String shopname) {
        this.money = money;
        this.sn = sn;
        this.createTime = createTime;
        this.userBalance = userBalance;
        this.shopname = shopname;
    }

    public TimeLineModel(BigDecimal money, String sn, Date createTime, BigDecimal userBalance, String shopname, String type) {
        this.money = money;
        this.sn = sn;
        this.createTime = createTime;
        this.userBalance = userBalance;
        this.shopname = shopname;
        this.type = type;
    }

    public TimeLineModel(BigDecimal money, String sn, Date createTime, BigDecimal userBalance, String shopname, String type, String note) {
        this.money = money;
        this.sn = sn;
        this.createTime = createTime;
        this.userBalance = userBalance;
        this.shopname = shopname;
        this.type = type;
        this.note = note;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
