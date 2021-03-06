package ruilelin.com.shifenlife.json;
import java.io.Serializable;


public class Data<T> implements Serializable {
    private int code;
    private T data;
    private String message;

    public Data() {

    }

    public Data(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
