package ruilelin.com.shifenlife.json;

import java.math.BigDecimal;

public class OnlineRechargeJson {
    private BigDecimal money;
    private String payWay;

    public OnlineRechargeJson() {

    }

    public OnlineRechargeJson(BigDecimal money, String payWay) {
        this.money = money;
        this.payWay = payWay;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public String getPayWay() {
        return payWay;
    }
}
