package ruilelin.com.shifenlife.json;

public class Search {
    private int supplierId;
    private String name;

    public Search() {

    }

    public Search(int supplierId, String name) {
        this.supplierId = supplierId;
        this.name = name;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
