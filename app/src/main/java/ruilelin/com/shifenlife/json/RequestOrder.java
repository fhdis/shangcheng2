package ruilelin.com.shifenlife.json;

public class RequestOrder {
    private String status;

    public RequestOrder() {
    }

    public RequestOrder(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
