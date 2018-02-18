package me.xp090.shuffledairy.design;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Xp090 on 24/01/2018.
 */

public class ShrinkableButton extends AppCompatButton {

    int width;
    int height;
    int shrinkWidth;
    int shrinkHeight;

    int marginX;
    int marginY;
    public ShrinkableButton(Context context) {
        super(context);
    }

    public ShrinkableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShrinkableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                width = getWidth();
                height = getHeight();
                shrinkWidth = (int)(width*0.85);
                shrinkHeight = (int)(height*0.85);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
                params.width = shrinkWidth;
                params.height = shrinkHeight;
                marginX = (width-shrinkWidth) /2;
                marginY = (height-shrinkHeight)/2;
                setShrinkMargin(params);
                setLayoutParams(params);
            } else if (action == MotionEvent.ACTION_UP) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
                params.width = width;
                params.height = height;
                params.setMargins(0,0,0,0);
                setLayoutParams(params);
                performClick();
            }
            return true;
        }
        return true;

    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void setShrinkMargin(FrameLayout.LayoutParams params) {
        switch (params.gravity) {
            case Gravity.LEFT:
                params.leftMargin= marginX;
                params.topMargin = marginY;
                break;
            case Gravity.RIGHT:
                params.rightMargin= marginX;
                params.topMargin = marginY;
                break;
        }

    }
}
