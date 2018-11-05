package ruilelin.com.shifenlife.json;
import java.io.Serializable;


public class Order implements Serializable {
    private String id;
    private String userId;
    private String sn;
    private String status;
    private String supplierId;
    private String createTime;
    private String price;
    private String note;

    public Order() {

    }

    public Order(String id) {
        this.id = id;
    }

    public Order(String id, String userId, String sn, String status, String supplierId, String createTime, String price, String note) {
        this.id = id;
        this.userId = userId;
        this.sn = sn;
        this.status = status;
        this.supplierId = supplierId;
        this.createTime = createTime;
        this.price = price;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
