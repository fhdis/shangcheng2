package ruilelin.com.shifenlife.listener;

import android.view.View;

public interface OnClickRecyclerViewListener {
    void onItemClick(int position, View view);
    boolean onItemLongClick(int position);
}
