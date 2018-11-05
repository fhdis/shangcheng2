package ruilelin.com.shifenlife.view;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ruilelin.com.shifenlife.R;

public class PaddingDecoration extends DividerItemDecoration {

    private int padding;

    /**
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public PaddingDecoration(Context context, int orientation) {
        super(context, orientation);
        padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_left);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = padding;
        outRect.right = padding;
        outRect.bottom = padding;
    }
}
