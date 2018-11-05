package ruilelin.com.shifenlife.json;

import java.io.Serializable;

public class HotProduct implements Serializable{
    private int id;
    private int categoryId;
    private String sn;
    private String name;
    private int number;
    private boolean piece;
    private String spec;
    private int marketPrice;
    private int price;
    private String description;
    private String thumbnail;
    private boolean best;
    private boolean newer;
    private boolean hot;
    private boolean recommend;
    private String detail;
    private boolean selling;
    private int sortOrder;
    private String createTime;
    private String updateTime;
    private int supplierId;
    private String qrcode;
    private String img;
    private String supplierName;
    private int sales;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public HotProduct() {

    }
    public HotProduct(int id) {
        this.id = id;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
    public String getSn() {
        return sn;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setPiece(boolean piece) {
        this.piece = piece;
    }
    public boolean getPiece() {
        return piece;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getSpec() {
        return spec;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }
    public int getMarketPrice() {
        return marketPrice;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setBest(boolean best) {
        this.best = best;
    }
    public boolean getBest() {
        return best;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }
    public boolean getNewer() {
        return newer;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }
    public boolean getHot() {
        return hot;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
    public boolean getRecommend() {
        return recommend;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getDetail() {
        return detail;
    }

    public void setSelling(boolean selling) {
        this.selling = selling;
    }
    public boolean getSelling() {
        return selling;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    public int getSortOrder() {
        return sortOrder;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    public int getSupplierId() {
        return supplierId;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    public String getQrcode() {
        return qrcode;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {
        return img;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public String getSupplierName() {
        return supplierName;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
    public int getSales() {
        return sales;
    }

}
