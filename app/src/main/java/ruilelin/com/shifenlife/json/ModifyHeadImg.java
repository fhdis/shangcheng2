package ruilelin.com.shifenlife.json;

public class ModifyHeadImg {
    private String nickname;
    private String headImg;

    public ModifyHeadImg() {
    }

    public ModifyHeadImg(String nickname, String headImg) {
        this.nickname = nickname;
        this.headImg = headImg;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
    public String getHeadImg() {
        return headImg;
    }
}
