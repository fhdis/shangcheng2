package ruilelin.com.shifenlife.json;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Primary implements Parcelable {
    private int id;
    private String name;
    private int pid;
    private int sortOrder;
    private boolean display;
    private boolean recommend;
    private String img;
    private String description;
    private List<HotParcelable> goods;
    //private int goodsNumber;


    public Primary() {

    }

    public Primary(List<HotParcelable> goods) {
        this.goods = goods;
    }

    public static final Creator<Primary> CREATOR = new Creator<Primary>() {
        @Override
        public Primary createFromParcel(Parcel in) {
            return new Primary(in);
        }

        @Override
        public Primary[] newArray(int size) {
            return new Primary[size];
        }
    };

    protected Primary(Parcel in) {
        name = in.readString();
        //goodsNumber = in.readInt();
        //tag = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(pid);
       // parcel.writeInt(goodsNumber);
        parcel.writeInt(sortOrder);
        parcel.writeByte((byte) (display ? 1 : 0));
        parcel.writeByte((byte) (recommend ? 1 : 0));
        parcel.writeString(img);
        parcel.writeString(description);
        parcel.writeTypedList(goods);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }*/

    public List<HotParcelable> getGoods() {
        return goods;
    }

    public void setGoods(List<HotParcelable> goods) {
        this.goods = goods;
    }
}
