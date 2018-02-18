package me.xp090.shuffledairy.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import me.xp090.shuffledairy.R;


/**
 * Created by Xp090 on 12/01/2018.
 */

public class TimeLineLink extends View {

    private Context mContext ;
    private float thickness;
    private float radius;
    private int lineColor;
    private int dotColor;
    private boolean hasDot;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public TimeLineLink(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public TimeLineLink(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public TimeLineLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        setWillNotDraw(false);
        if (attrs != null) {
            TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.TimeLineLink,0,0);
            thickness = array.getDimension(R.styleable.TimeLineLink_lineThickness, 4f);
            lineColor = array.getColor(R.styleable.TimeLineLink_lineColor, Color.WHITE);
            dotColor = array.getColor(R.styleable.TimeLineLink_dotColor, Color.WHITE);
            radius = array.getDimension(R.styleable.TimeLineLink_radius,12f);
            hasDot = array.getBoolean(R.styleable.TimeLineLink_hasDot, false);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) radius;
        int desiredHeight = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = (float) getWidth() /2;
        float y = (float) getHeight();
        paint.setColor(lineColor);
      //  canvas.drawRect(x - thickness /2, 0f, x + thickness /2, y, paint);
        if (hasDot) {
            paint.setColor(dotColor);

            canvas.drawCircle(x , (y/2), radius/2 , paint);
        }

    }
}
