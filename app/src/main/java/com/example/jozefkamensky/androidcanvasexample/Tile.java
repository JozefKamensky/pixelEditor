package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

public class Tile {
    private int startX;
    private int startY;
    private Paint paint;
    private boolean isTransparent;
    private int[] dividePoints;

    public Tile(int x, int y, int tileSize) {
        this.startX = x;
        this.startY = y;
        paint = new Paint();
        setColor(255, 255, 255, 0);
        isTransparent = true;
        //4 squares, for each 2 points with 2 coordinates
        dividePoints = new int[4*2*2];
        int edge = tileSize / 2;
        dividePoints[0] = startX;
        dividePoints[1] = startY;
        dividePoints[2] = startX + edge;
        dividePoints[3] = startY + edge;

        dividePoints[4] = startX + edge;
        dividePoints[5] = startY;
        dividePoints[6] = dividePoints[4] + edge;
        dividePoints[7] = startY + edge;

        dividePoints[8] = startX;
        dividePoints[9] = startY + edge;
        dividePoints[10] = startX + edge;
        dividePoints[11] = dividePoints[9] + edge;

        dividePoints[12] = startX + edge;
        dividePoints[13] = startY + edge;
        dividePoints[14] = dividePoints[12] + edge;
        dividePoints[15] = dividePoints[13] + edge;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setColor(int R, int G, int B, int A){
        int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
        paint.setColor(color);
        isTransparent = (A == 0);
    }

    public void setPaint(int color){
        paint.setColor(color);
        Log.d("TILE", "setPaint to color: " + paint.getColor());
    }

    public Paint getPaint(){
        return paint;
    }

    public Rect getTileAsRect(){
        return new Rect(startX, startY, dividePoints[14], dividePoints[15]);
    }

    public RectF getTileAsRectF(){
        return new RectF(startX, startY, dividePoints[14], dividePoints[15]);
    }

    public List<Rect> getTileAsRects(){
        List<Rect> rectangles = new ArrayList<>();
        for (int i = 0; i < dividePoints.length/4; i++){
            rectangles.add(new Rect(dividePoints[i * 4], dividePoints[i * 4 + 1], dividePoints[i * 4 + 2], dividePoints[i * 4 + 3]));
        }
        return rectangles;
    }

    public boolean isTransparent() {
        return isTransparent;
    }
}
