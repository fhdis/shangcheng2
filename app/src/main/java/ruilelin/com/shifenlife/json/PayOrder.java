package ruilelin.com.shifenlife.json;

import java.io.Serializable;
import java.util.List;

public class PayOrder implements Serializable{

    private String sn;
    private String status;
    private String supplierName;
    private String id;
    private String createTime;
    private String price;
    private List<Goods> goods;

    public PayOrder() {
    }

    public PayOrder(String sn, String status, String supplierName, String id, String createTime, String price, List<Goods> goods) {
        this.sn = sn;
        this.status = status;
        this.supplierName = supplierName;
        this.id = id;
        this.createTime = createTime;
        this.price = price;
        this.goods = goods;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public static class Goods implements Serializable {
        private String goodsName;
        private String goodsNumber;
        private String goodsPrice;
        private String spec;

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
}
