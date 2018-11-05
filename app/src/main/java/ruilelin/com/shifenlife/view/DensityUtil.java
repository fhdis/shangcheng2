package ruilelin.com.shifenlife.view;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DensityUtil {
    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }
}