package ruilelin.com.shifenlife.person.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class PressDarkButton extends AppCompatButton {

    public final float[] BT_NOT_SELECTED = { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 1.0F, 0.0F };
    public final float[] BT_SELECTED = { 1.0F, 0.0F, 0.0F, 0.0F, -50.0F, 0.0F,
            1.0F, 0.0F, 0.0F, -50.0F, 0.0F, 0.0F, 1.0F, 0.0F, -50.0F, 0.0F,
            0.0F, 0.0F, 1.0F, 0.0F };

    public PressDarkButton(Context paramContext) {
        super(paramContext);
    }

    public PressDarkButton(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public PressDarkButton(Context paramContext,
                           AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (isEnabled()) {
                getBackground().setColorFilter(new ColorMatrixColorFilter(this.BT_SELECTED));
                setBackgroundDrawable(getBackground());
            }
        } else if (event.getAction() == 1) {
            getBackground().setColorFilter(new ColorMatrixColorFilter(this.BT_NOT_SELECTED));
            setBackgroundDrawable(getBackground());
        }
        return super.dispatchTouchEvent(event);
    }

}
