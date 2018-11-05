package ruilelin.com.shifenlife.json;

import ruilelin.com.shifenlife.pay.ali.PayWay_ALI;

public class PayWayReply {
    private int code;
    private String message;
    private PayWay_ALI data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(PayWay_ALI data) {
        this.data = data;
    }
    public PayWay_ALI getData() {
        return data;
    }
}
