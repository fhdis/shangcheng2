package ruilelin.com.shifenlife.json;

public class BannerRequest {
    private int supplierId;
    private String type;

    public BannerRequest() {
    }

    public BannerRequest(int supplierId, String type) {
        this.supplierId = supplierId;
        this.type = type;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
