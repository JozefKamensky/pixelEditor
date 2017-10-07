package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.RectF;

/**
 * Created by jozef.kamensky on 5.10.2017.
 * Class representing one tile of grid.
 */

public class Tile {
    // x and y coordinate in the grid of tiles
    private int x;
    private int y;

    // rectangle representing tile (with screen coordinates)
    // used to speed up drawing
    private RectF rectangle;

    // int representation of color of tile
    private int color;

    // true if tile is fully transparent (A channel equals 0)
    private boolean transparent;

    public Tile(int x, int y, int tileSize, int startX, int startY, int color){
        this.x = x;
        this.y = y;
        setColor(color);
        rectangle = new RectF(startX, startY, startX + tileSize, startY + tileSize);
    }

    public RectF getRectangle(){
        return rectangle;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
        updateTransparency();
    }

    // method checking transparency of tile
    // used whenever tile color is changed
    private void updateTransparency(){
        int a = (color >> 24) & 0xff;
        transparent = (a == 0);
    }

    public boolean isTransparent(){
        return transparent;
    }

}
