package ruilelin.com.shifenlife.json;

import java.io.Serializable;
import java.util.List;

public class ProductDetail implements Serializable{
    private String id;
    private String categoryId;
    private String sn;
    private String name;
    private String number;
    private boolean piece;
    private String spec;
    private String marketPrice;
    private String price;
    private String description;
    private String thumbnail;
    private boolean best;
    private boolean newer;
    private boolean hot;
    private boolean recommend;
    private String detail;
    private boolean selling;
    private String sortOrder;
    private String createTime;
    private String updateTime;
    private String supplierId;
    private String qrcode;
    private List<Imgs> imgs;
    private String supplierName;
    private String mailingDescription;
    private String distributionDescription;
    private String sales;

    public ProductDetail() {

    }

    public ProductDetail(String id, String categoryId, String sn, String name, String number, boolean piece, String spec, String marketPrice, String price, String description, String thumbnail, boolean best, boolean newer, boolean hot, boolean recommend, String detail, boolean selling, String sortOrder, String createTime, String updateTime, String supplierId, String qrcode, List<Imgs> imgs, String supplierName, String mailingDescription, String distributionDescription, String sales) {
        this.id = id;
        this.categoryId = categoryId;
        this.sn = sn;
        this.name = name;
        this.number = number;
        this.piece = piece;
        this.spec = spec;
        this.marketPrice = marketPrice;
        this.price = price;
        this.description = description;
        this.thumbnail = thumbnail;
        this.best = best;
        this.newer = newer;
        this.hot = hot;
        this.recommend = recommend;
        this.detail = detail;
        this.selling = selling;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.supplierId = supplierId;
        this.qrcode = qrcode;
        this.imgs = imgs;
        this.supplierName = supplierName;
        this.mailingDescription = mailingDescription;
        this.distributionDescription = distributionDescription;
        this.sales = sales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isSelling() {
        return selling;
    }

    public void setSelling(boolean selling) {
        this.selling = selling;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<Imgs> getImgs() {
        return imgs;
    }

    public void setImgs(List<Imgs> imgs) {
        this.imgs = imgs;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMailingDescription() {
        return mailingDescription;
    }

    public void setMailingDescription(String mailingDescription) {
        this.mailingDescription = mailingDescription;
    }

    public String getDistributionDescription() {
        return distributionDescription;
    }

    public void setDistributionDescription(String distributionDescription) {
        this.distributionDescription = distributionDescription;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public static  class Imgs implements Serializable {
        private String id;
        private String goodsId;
        private String img;

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
    }
}
