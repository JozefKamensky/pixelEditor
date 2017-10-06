package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 5.10.2017.
 */
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class TestTile {
    //x and y coordinate in the grid of tiles
    private int x;
    private int y;
    private List<RectF> rectangles;
    private int color;

    public TestTile(int x, int y, int tileSize, int startX, int startY, int color){
        this.x = x;
        this.y = y;
        this.color = color;

        rectangles = new ArrayList<>();
        int edge = tileSize / 2;

        //top left rectangle
        rectangles.add(new RectF(startX, startY, startX + edge, startY + edge));
        //top right rectangle
        rectangles.add(new RectF(startX + edge, startY, startX + 2*edge, startY + edge));
        //bottom right rectangle
        rectangles.add(new RectF(startX + edge, startY + edge, startX + 2*edge, startY + 2*edge));
        //bottom left rectangle
        rectangles.add(new RectF(startX, startY + edge, startX + edge, startY + 2*edge));
    }

    private TestTile(int x, int y, List<RectF> rects, int color){
        this.x = x;
        this.y = y;
        this.rectangles = rects;
        this.color = color;
    }

    public List<RectF> getRectangles(){
        return rectangles;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public TestTile clone(){
        return new TestTile(this.x, this.y, this.rectangles, this.color);
    }

}
