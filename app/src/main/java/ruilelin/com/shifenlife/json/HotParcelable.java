package ruilelin.com.shifenlife.json;

import android.os.Parcel;
import android.os.Parcelable;

public class HotParcelable implements Parcelable {

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

    public HotParcelable() {

    }

    public static final Creator<HotParcelable> CREATOR = new Creator<HotParcelable>() {
        @Override
        public HotParcelable createFromParcel(Parcel in) {
            return new HotParcelable(in);
        }

        @Override
        public HotParcelable[] newArray(int size) {
            return new HotParcelable[size];
        }
    };

    protected HotParcelable(Parcel in) {
        id = in.readInt();
        categoryId = in.readInt();
        number = in.readInt();
        marketPrice = in.readInt();
        name = in.readString();
        sn = in.readString();
        spec = in.readString();
        piece = in.readByte()!=0;
        thumbnail = in.readString();
        img = in.readString();
        //goodsNumber = in.readInt();
        //tag = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img);
        parcel.writeString(thumbnail);
        parcel.writeString(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
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

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}
