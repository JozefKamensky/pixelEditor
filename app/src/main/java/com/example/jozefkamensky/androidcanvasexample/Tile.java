package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

public class Tile {
    private int x;
    private int y;
    private Paint paint;

    public Tile( int x, int y){
        this.x = x;
        this.y = y;
        paint = new Paint();
        setColor(255,255,255, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setColor(int R, int G, int B, int A){
        int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
        paint.setColor(color);
    }

    public void setPaint(int color){
        paint.setColor(color);
    }

    public Paint getPaint(){
        return paint;
    }

    public RectF getTileAsRect(int tileSize){
        return new RectF(x,y, x + tileSize, y + tileSize);
    }
}
