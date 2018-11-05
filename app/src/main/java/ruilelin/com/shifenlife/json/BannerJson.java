package ruilelin.com.shifenlife.json;

public class BannerJson {
    private String id;
    private String goodsId;
    private String img;
    private String advertisingType;
    private String supplierId;

    public BannerJson() {

    }

    public BannerJson(String img) {
        this.img = img;
    }

    public BannerJson(String id, String goodsId, String img, String advertisingType, String supplierId) {
        this.id = id;
        this.goodsId = goodsId;
        this.img = img;
        this.advertisingType = advertisingType;
        this.supplierId = supplierId;
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAdvertisingType() {
        return advertisingType;
    }

    public void setAdvertisingType(String advertisingType) {
        this.advertisingType = advertisingType;
    }
}
