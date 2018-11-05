package ruilelin.com.shifenlife.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.TextView;

import ruilelin.com.shifenlife.R;

/**
 * 倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {

    private final Context mContext;
    private TextView mTextView;

    /**
     * @param context           上下文
     * @param textView          视图View
     * @param millisInFuture    倒计时完成时间，完成之后调用onFinish()
     * @param countDownInterval 获取onTick()回调的间隔
     */
    public CountDownTimerUtils(Context context,TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mContext = context;
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);//设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s");//设置倒计时时间
        mTextView.setBackgroundResource(R.drawable.bg_identify_code_press);//背景色
        mTextView.setTextSize(10);//字体大小
        mTextView.setGravity(Gravity.CENTER);//文字居中显示
        int color = mContext.getResources().getColor(R.color.hint_color);
//        mTextView.setTextColor(color);//字体颜色


        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());//获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(color);

        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("再次获取");
        int color = mContext.getResources().getColor(R.color.yellow_line);
        mTextView.setTextColor(color);//字体颜色
        mTextView.setClickable(true);//重新获取点击事件

        mTextView.setBackgroundResource(R.drawable.round_corner_yellow_stroke);//还原背景色
    }
}
