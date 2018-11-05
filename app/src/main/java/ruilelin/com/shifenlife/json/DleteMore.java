package ruilelin.com.shifenlife.json;

import java.util.List;

public class DleteMore {
    public DleteMore() {
    }

    public DleteMore(List<Integer> ids) {
        this.ids = ids;
    }

    private List<Integer> ids;
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
    public List<Integer> getIds() {
        return ids;
    }
}
