package ruilelin.com.shifenlife.json;

public class AddToCart {
    private int goodsId;
    private int number;

    public AddToCart() {
    }

    public AddToCart(int goodsId, int number) {
        this.goodsId = goodsId;
        this.number = number;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}
