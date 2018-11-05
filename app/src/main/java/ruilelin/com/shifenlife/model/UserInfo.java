package ruilelin.com.shifenlife.model;

public class UserInfo {
    private int id;
    private String balance;
    private int coin;
    private String headImg;
    private int level;
    private String mobile;
    private String nickname;
    private String sex;
    private String wxUnionId;

    public UserInfo() {
    }

    public UserInfo(int id, String balance, int coin, String headImg, int level, String mobile, String nickname, String sex, String wxUnionId) {
        this.id = id;
        this.balance = balance;
        this.coin = coin;
        this.headImg = headImg;
        this.level = level;
        this.mobile = mobile;
        this.nickname = nickname;
        this.sex = sex;
        this.wxUnionId = wxUnionId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
    public int getCoin() {
        return coin;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
    public String getHeadImg() {
        return headImg;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
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

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }
    public String getWxUnionId() {
        return wxUnionId;
    }
}
