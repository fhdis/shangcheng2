package ruilelin.com.shifenlife.person.adapter;

import java.io.Serializable;

public class ItemModel implements Serializable {

    public static final int ONE = 1001;
    public static final int TWO = 1002;

    public int type;
    public Object data;

    public ItemModel(int type, Object data) {
        this.type = type;
        this.data = data;
    }
}
