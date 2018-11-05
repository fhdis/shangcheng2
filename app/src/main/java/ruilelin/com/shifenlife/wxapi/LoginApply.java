package ruilelin.com.shifenlife.wxapi;

public class LoginApply {
    private String wxUnionId;
    private String nickname;
    private String sex;
    private String headImg;

    public LoginApply() {

    }

    public LoginApply(String wxUnionId, String nickname, String sex, String headImg) {
        this.wxUnionId = wxUnionId;
        this.nickname = nickname;
        this.sex = sex;
        this.headImg = headImg;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }
    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
    public String getHeadImg() {
        return headImg;
    }
}