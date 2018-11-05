package ruilelin.com.shifenlife.json;

public class PasswordRequest {

    private String oldPassword;
    private String password;
    private String code;

    public PasswordRequest() {
    }

    public PasswordRequest(String password) {
        this.password = password;
    }

    public PasswordRequest(String password, String code) {
        this.password = password;
        this.code = code;
    }

    public PasswordRequest(String oldPassword, String password, String code) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "PasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
