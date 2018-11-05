package ruilelin.com.shifenlife.json;

public class VerCodeJson {

    private String type;
    private String mobile;

    public VerCodeJson() {

    }

    public VerCodeJson(String type, String mobile) {
        this.type = type;
        this.mobile = mobile;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

}
