package ruilelin.com.shifenlife.myorder.anotherway;

public class OrderGoodsFooter {
    private String createTime;
    private String price;
    private String beizhu;
    private String num;
    private String status;
    private String id;
    private String sn;

    public OrderGoodsFooter() {
    }

    public OrderGoodsFooter(String createTime, String price, String beizhu,String num,String status) {
        this.createTime = createTime;
        this.price = price;
        this.beizhu = beizhu;
        this.num = num;
        this.status =status;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
