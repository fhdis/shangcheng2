package ruilelin.com.shifenlife.json;

import java.util.List;

public class CreateOrderNote {
    private String note;
    private List<CreateOrder> goodsList;

    public CreateOrderNote() {
    }

    public CreateOrderNote(String note, List<CreateOrder> goodsList) {
        this.note = note;
        this.goodsList = goodsList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<CreateOrder> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CreateOrder> goodsList) {
        this.goodsList = goodsList;
    }
}
