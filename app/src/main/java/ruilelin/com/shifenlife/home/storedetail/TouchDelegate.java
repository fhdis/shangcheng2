package ruilelin.com.shifenlife.home.storedetail;

import android.view.View;

public abstract class TouchDelegate {

    public enum TouchOrientation{
        UP_2_DOWN,
        DOWN_2_UP
    }

    abstract void onTouchDone(View view, TouchOrientation orientation, float offset);
}
