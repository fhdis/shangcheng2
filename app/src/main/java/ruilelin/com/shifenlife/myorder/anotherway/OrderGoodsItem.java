package ruilelin.com.shifenlife.myorder.anotherway;

public class OrderGoodsItem {

    private String goodsName;
    private String goodsNumber;
    private String goodsPrice;
    private String spec;

    public OrderGoodsItem() {

    }

    public OrderGoodsItem(String goodsName, String goodsNumber, String goodsPrice, String spec) {
        this.goodsName = goodsName;
        this.goodsNumber = goodsNumber;
        this.goodsPrice = goodsPrice;
        this.spec = spec;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
