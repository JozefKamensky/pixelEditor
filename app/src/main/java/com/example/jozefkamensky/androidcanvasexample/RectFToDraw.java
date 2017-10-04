package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by jozef.kamensky on 4.10.2017.
 */

public class RectFToDraw {
    private RectF rect;
    private Paint paint;

    public RectFToDraw(RectF rectF, Paint paint){
        setRect(rectF);
        setPaint(paint);
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
