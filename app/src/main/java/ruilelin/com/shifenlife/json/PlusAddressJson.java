package ruilelin.com.shifenlife.json;

public class PlusAddressJson {

    private String consignee;
    private String sex;
    private String mobile;
    private boolean defaultAddress;
    private String address;
    private double lat;
    private double lng;
    private String looseAddress;

    public PlusAddressJson(){

    }
    public PlusAddressJson(String consignee, String sex, String mobile, boolean defaultAddress, String address, double lat, double lng, String looseAddress) {
        this.consignee = consignee;
        this.sex = sex;
        this.mobile = mobile;
        this.defaultAddress = defaultAddress;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.looseAddress = looseAddress;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }
    public String getConsignee() {
        return consignee;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
    public boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }

    public void setLooseAddress(String looseAddress) {
        this.looseAddress = looseAddress;
    }
    public String getLooseAddress() {
        return looseAddress;
    }

}
