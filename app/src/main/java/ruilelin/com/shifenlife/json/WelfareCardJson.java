package ruilelin.com.shifenlife.json;

public class WelfareCardJson {

    private String cardNumber;
    private String password;

    public WelfareCardJson() {

    }

    public WelfareCardJson(String cardNumber, String password) {
        this.cardNumber = cardNumber;
        this.password = password;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

}
