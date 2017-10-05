package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 5.10.2017.
 */
import android.graphics.Paint;

public class RectToDraw {
    public int top;
    public int left;
    public int bottom;
    public int right;
    public Paint paint;

    public RectToDraw(int t, int l, int b, int r, Paint p){
        this.top = t;
        this.left = l;
        this.bottom = b;
        this.right = r;
        this.paint = p;
    }
}
