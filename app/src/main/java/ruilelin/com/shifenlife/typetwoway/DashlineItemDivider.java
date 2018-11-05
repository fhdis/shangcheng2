package ruilelin.com.shifenlife.typetwoway;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.graphics.Path;

public class DashlineItemDivider extends RecyclerView.ItemDecoration {
    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;

            //绘制虚线
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            Path path = new Path();
            path.moveTo(30, top);
            path.lineTo(220,top);
            android.util.Log.d("xuxian","left=="+left);
            android.util.Log.d("xuxian","right=="+right);
            android.util.Log.d("xuxian","top=="+top);
            PathEffect effects = new DashPathEffect(new float[]{1,2,4,8},3);//此处单位是像素不是dp  注意 请自行转化为dp
            paint.setPathEffect(effects);
            c.drawPath(path, paint);


        }
    }

}