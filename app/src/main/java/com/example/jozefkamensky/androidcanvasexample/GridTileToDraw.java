package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

public class GridTileToDraw {

    private RectF rect;
    private Paint paint;

    public GridTileToDraw(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
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

    public void setColor(int R, int G, int B, int A) {
        int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 16 | (B & 0xff);
        this.paint.setColor(color);
    }
}
