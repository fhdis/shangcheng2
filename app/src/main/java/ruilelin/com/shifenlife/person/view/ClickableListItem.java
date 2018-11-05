package ruilelin.com.shifenlife.person.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ruilelin.com.shifenlife.R;

public class ClickableListItem extends LinearLayout  {


    private boolean containIcon;


    private String title;


    private int iconSrc;


    private int textColor;


    private int textSize;


    private LinearLayout container;


    // px
    private int paddingContainer = dip2px(getContext(), 10);


    private int tvPadding = dip2px(getContext(), 5);


    public ClickableListItem(Context context) {
        super(context);
        init();
    }


    public ClickableListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ClickableListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickableListItem, defStyle, 0);


        containIcon = a.getBoolean(R.styleable.ClickableListItem_containIcon, false);


        title = a.getString(R.styleable.ClickableListItem_text);


        iconSrc = a.getResourceId(R.styleable.ClickableListItem_imgsrc, R.mipmap.ic_action_next);


        textColor = a.getColor(R.styleable.ClickableListItem_textColor, R.color.gray);


        textSize = a.getDimensionPixelSize(R.styleable.ClickableListItem_textSize, 14);


        a.recycle();


        init();


    }


    private void init() {


        container = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(params);
        container.setGravity(Gravity.CENTER_VERTICAL);
        container.setPadding(paddingContainer, paddingContainer, paddingContainer, paddingContainer);
        container.setBackgroundResource(R.color.white);
        // 添加tv元素
        TextView tv = new TextView(getContext());
        LayoutParams tvParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParam.weight = 1;
        tv.setLayoutParams(tvParam);


        tv.setPadding(tvPadding, tvPadding, tvPadding, tvPadding);
        tv.setText(title);
        tv.setTextColor(textColor);
        tv.setTextSize(px2sp(getContext(), textSize));
        tv.setGravity(Gravity.CENTER_VERTICAL);


        if (containIcon) {
            Drawable d = getContext().getDrawable(iconSrc);
            d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());// 必须设置图片大小，否则不显示
            tv.setCompoundDrawables(d, null, null, null);
        }
        // 添加img元素
        ImageView iv = new ImageView(getContext());
        LayoutParams ivParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(ivParam);
        iv.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_right));
        //
        container.addView(tv);
        container.addView(iv);


        addView(container);
    }


    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}

