package ruilelin.com.shifenlife.json;

import java.io.Serializable;

public class ProductDetailJson implements Serializable{
    private String supplierId;
    private String id;

    public ProductDetailJson() {

    }

    public ProductDetailJson(String supplierId, String id) {
        this.supplierId = supplierId;
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
