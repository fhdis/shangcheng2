package ruilelin.com.shifenlife.json;

public class LoginWithPasswordJson {
    private String mobile;
    private String password;

    public LoginWithPasswordJson() {
    }

    public LoginWithPasswordJson(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
