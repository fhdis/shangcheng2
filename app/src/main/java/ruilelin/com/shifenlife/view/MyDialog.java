package ruilelin.com.shifenlife.view;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ruilelin.com.shifenlife.R;

public class MyDialog {

    //点击确认按钮回调接口
    public interface OnConfirmListener {
        void onConfirmClick();
    }

    /**
     * @param activity
     * @param content         提示内容
     * @param confirmListener void
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity, String content,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog_two_btn, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);

        if (!StringUtil.isNullOrEmpty(content)) {
            tvMessage.setText(content);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 3 * 2,
                RelativeLayout.LayoutParams.WRAP_CONTENT);//设置弹出框宽度为屏幕宽度的三分之二

        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
