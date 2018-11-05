package ruilelin.com.shifenlife.cart.bean;

import java.io.Serializable;
import java.util.List;

public class ShoppingCarDataBean implements Serializable {

    private int code;
    private String message;
    private List<DatasBean> data;


    public ShoppingCarDataBean() {
    }

    public ShoppingCarDataBean(int code, String message, List<DatasBean> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DatasBean> getData() {
        return data;
    }

    public void setData(List<DatasBean> data) {
        this.data = data;
    }

    public static class DatasBean implements Serializable{
        private Supplier supplier;
        private List<Commodity> goods;

        public Supplier getSupplier() {
            return supplier;
        }

        public void setSupplier(Supplier supplier) {
            this.supplier = supplier;
        }

        public List<Commodity> getGoods() {
            return goods;
        }

        public void setGoods(List<Commodity> goods) {
            this.goods = goods;
        }

        public static class Supplier implements Serializable{
            private String name;
            private double lat;
            private double lng;
            private boolean isSelect_shop;      //店铺是否在购物车中被选中

            public boolean isSelect_shop() {
                return isSelect_shop;
            }

            public void setSelect_shop(boolean select_shop) {
                isSelect_shop = select_shop;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
        public static class Commodity  implements Serializable {
            private String id;
            private String name;
            private String number;
            private boolean piece;
            private String spec;
            private String marketPrice;
            private String price;
            private String thumbnail;
            private String createTime;
            private String supplierId;
            private boolean isSelect;        //商品是否在购物车中被选中
            private String suppliername;


            public Commodity() {
            }

            public Commodity(String id, String name, String number, boolean piece, String spec, String marketPrice, String price, String thumbnail, String createTime, String supplierId, boolean isSelect, String suppliername) {
                this.id = id;
                this.name = name;
                this.number = number;
                this.piece = piece;
                this.spec = spec;
                this.marketPrice = marketPrice;
                this.price = price;
                this.thumbnail = thumbnail;
                this.createTime = createTime;
                this.supplierId = supplierId;
                this.isSelect = isSelect;
                this.suppliername = suppliername;
            }

            public String getSuppliername() {
                return suppliername;
            }

            public void setSuppliername(String suppliername) {
                this.suppliername = suppliername;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public boolean isPiece() {
                return piece;
            }

            public void setPiece(boolean piece) {
                this.piece = piece;
            }

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getSupplierId() {
                return supplierId;
            }

            public void setSupplierId(String supplierId) {
                this.supplierId = supplierId;
            }
        }
    }

}

