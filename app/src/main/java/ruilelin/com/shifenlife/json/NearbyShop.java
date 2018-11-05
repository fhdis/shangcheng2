package ruilelin.com.shifenlife.json;

import java.io.Serializable;

public class NearbyShop implements Serializable{
    private String supplierImg;
    private String name;
    private double lat;
    private double lng;
    private double distance;
    private int dispatchingPrice;
    private double shippingFee;
    private int monthlySales;
    private int id;
    private String address;
    private String mobile;


    public NearbyShop() {
    }

    public NearbyShop(String supplierImg, String name, double lat, double lng, double distance, int dispatchingPrice, double shippingFee, int monthlySales,int id) {
        this.supplierImg = supplierImg;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.dispatchingPrice = dispatchingPrice;
        this.shippingFee = shippingFee;
        this.monthlySales = monthlySales;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierImg() {
        return supplierImg;
    }

    public void setSupplierImg(String supplierImg) {
        this.supplierImg = supplierImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDispatchingPrice() {
        return dispatchingPrice;
    }

    public void setDispatchingPrice(int dispatchingPrice) {
        this.dispatchingPrice = dispatchingPrice;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(int monthlySales) {
        this.monthlySales = monthlySales;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
