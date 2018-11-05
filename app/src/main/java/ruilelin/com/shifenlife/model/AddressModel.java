package ruilelin.com.shifenlife.model;

import java.io.Serializable;

public class AddressModel implements Serializable {

    private int id;
    private int userId;
    private String consignee;
    private String sex;
    private int country;
    private String province;
    private String city;
    private String district;
    private String looseAddress;
    private String address;
    private String mobile;
    private boolean defaultAddress;
    private String createTime;
    private double lat;
    private double lng;

    public AddressModel(){

    }

    public AddressModel(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public AddressModel(String looseAddress) {
        this.looseAddress = looseAddress;
    }

    public AddressModel(int id, int userId, String consignee, String sex, int country, String province, String city, String district, String looseAddress, String address, String mobile, boolean defaultAddress, String createTime, double lat, double lng) {
        this.id = id;
        this.userId = userId;
        this.consignee = consignee;
        this.sex = sex;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.looseAddress = looseAddress;
        this.address = address;
        this.mobile = mobile;
        this.defaultAddress = defaultAddress;
        this.createTime = createTime;
        this.lat = lat;
        this.lng = lng;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
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

    public void setCountry(int country) {
        this.country = country;
    }
    public int getCountry() {
        return country;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDistrict() {
        return district;
    }

    public void setLooseAddress(String looseAddress) {
        this.looseAddress = looseAddress;
    }
    public String getLooseAddress() {
        return looseAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

}
