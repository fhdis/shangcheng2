package ruilelin.com.shifenlife.home.moremenu;

import java.io.Serializable;

public class SFSHCategory implements Serializable {

    private String categoryName;//按钮名字
    private String Image;//按钮图片
    private int categoryId;//按钮 id

    public SFSHCategory() {

    }

    public SFSHCategory(String categoryName, String image) {
        this.categoryName = categoryName;
        Image = image;
    }

    public SFSHCategory(String categoryName, String image, int categoryId) {
        this.categoryName = categoryName;
        Image = image;
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}


