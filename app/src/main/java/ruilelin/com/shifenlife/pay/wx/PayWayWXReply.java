package ruilelin.com.shifenlife.pay.wx;

import java.util.Map;

public class PayWayWXReply {
    private int code;
    private String message;
    private Map<String, Map<String, String>> data;

    public PayWayWXReply() {

    }

    public PayWayWXReply(int code, String message, Map<String, Map<String, String>> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

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

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }
    public Map<String, Map<String, String>> getData() {
        return data;
    }

}
