package ruilelin.com.shifenlife.json;

public class LoginWithTelAndVer {
    private String mobile;
    private String code;

    public LoginWithTelAndVer() {

    }

    public LoginWithTelAndVer(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
