package ruilelin.com.shifenlife.json;

public class PayWayJson {
    private int id;
    private String payWay;


    public PayWayJson() {

    }

    public PayWayJson(int id, String payWay) {
        this.id = id;
        this.payWay = payWay;
    }



    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public String getPayWay() {
        return payWay;
    }
}
