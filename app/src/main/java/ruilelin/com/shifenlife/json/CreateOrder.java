package ruilelin.com.shifenlife.json;
import java.io.Serializable;


public class CreateOrder implements Serializable {
    private String id;
    private String price;

    public CreateOrder() {
    }

    public CreateOrder(String id, String price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
