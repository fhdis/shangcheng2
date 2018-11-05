package ruilelin.com.shifenlife.json;

import java.io.Serializable;
import java.util.Date;

public class Infomation implements Serializable {

    private int id;
    private String message;
    private boolean readout;
    private Date createTime;

    public Infomation() {

    }

    public Infomation(int id, String message, boolean readout, Date createTime) {
        this.id = id;
        this.message = message;
        this.readout = readout;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getReadout() {
        return readout;
    }

    public void setReadout(boolean readout) {
        this.readout = readout;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
