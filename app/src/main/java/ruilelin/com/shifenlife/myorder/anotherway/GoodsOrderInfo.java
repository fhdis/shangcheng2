package ruilelin.com.shifenlife.myorder.anotherway;

public class GoodsOrderInfo {
    private String sn;
    private String supplierName;
    private String status;

    public GoodsOrderInfo() {
    }

    public GoodsOrderInfo(String sn, String supplierName, String status) {
        this.sn = sn;
        this.supplierName = supplierName;
        this.status = status;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
