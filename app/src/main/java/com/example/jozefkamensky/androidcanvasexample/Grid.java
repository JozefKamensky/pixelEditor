package com.example.jozefkamensky.androidcanvasexample;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jozef.kamensky on 20.9.2017.
 * Class representing grid of tiles and used for manipulation with tiles.
 */

public class Grid {

    // size of one square tile in pixels
    private int tileSize;

    // dimensions in tilesForColor
    private int width;
    private int height;

    // screen x and y coordinates of top left corner of grid
    private int startX;
    private int startY;

    // list of tiles of grid containing width*height tiles
    private List<Tile> tiles;

    // precalculated array of coordinates of lines between tiles in grid
    private float[] gridLinesCoordinates;

    public Grid(int widthInTiles, int heightInTiles, int viewWidth, int viewHeight){

        // minimal values of padding so grid is not situated too close
        // to edges of screen
        int minPaddingHorizontal = 50;
        int minPaddingVertical = 100;

        this.width = widthInTiles;
        this.height = heightInTiles;

        // calculate maximum tileSize in pixels to fit grid on screen
        int tileSize1 = (viewWidth - 2 * minPaddingHorizontal) / widthInTiles;
        int tileSize2 = (viewHeight - 2 * minPaddingVertical) / heightInTiles;
        this.tileSize = (tileSize1 < tileSize2) ? tileSize1 : tileSize2;

        startX = (viewWidth - widthInTiles * tileSize) / 2;
        startY = (viewHeight - heightInTiles * tileSize) / 2;

        // all tiles are created and set as transparent
        tiles = new ArrayList<>();
        int transparentWhite = RGBtoIntColor(255,255,255, 0);
        for (int i = 0; i < heightInTiles; i++){
            for (int j = 0; j < widthInTiles; j++){
                Tile t = new Tile(j, i, tileSize,
                        startX + j * tileSize, startY + i * tileSize , transparentWhite);
                tiles.add(t);
            }
        }
        gridLinesCoordinates = calculateGridLinesCoordinates();
    }

    // method for changing color of tile
    // method return index of changed tile in tiles array
    public int changeTileColor(float x, float y, int newColor){
        // transform screen coordintes to grid coordinates
        int tileX = ((int)x - startX) / tileSize;
        int tileY = ((int)y - startY) / tileSize;

        // user clicked out of grid so return -1 (used as signal that
        // there is no need to change tile a redraw it)
        if (tileX < 0 || tileX >= width) return -1;
        if (tileY < 0 || tileY >= height) return - 1;

        // calculate index of tile with tileX and tileY coordinates
        int tileIndex = tileY * width + tileX;

        // we want only change oldColor of tile to newColor if they are different
        int oldColor = tiles.get(tileIndex).getColor();
        if (oldColor != newColor){
            tiles.get(tileIndex).setColor(newColor);
        }
        // if not return -1 (no change needed)
        else tileIndex = -1;

        if(tileIndex == - 1) Log.d("GRID", "changeTileColor: Same color, no need to redraw.");
        else Log.d("GRID", "changeTileColor: New color, need to redraw tile.");

        return tileIndex;
    }

    // method for calculating coordinates of lines that separates tiles in grid
    private float[] calculateGridLinesCoordinates(){

        // calculate grid dimension in pixels
        int gridWidthInPixels = width * tileSize - 1;
        int gridHeightInPixels = height * tileSize - 1;
        int i;

        // array to store calculated coordinates
        float[] linesCoordinates = new float[4 * (width + height + 2)];
        int c = 0;

        // horizontal lines - only y coordinate is changing
        // set starting and ending position
        float sx = startX;
        float sy = startY;
        float ex = startX + gridWidthInPixels;
        float ey = startY;

        // we need to draw height + 1 lines
        for (i = 0; i <= height; i++){
            linesCoordinates[c++] = sx;
            linesCoordinates[c++] = sy;
            linesCoordinates[c++] = ex;
            linesCoordinates[c++] = ey;

            //change y coordinate to move to next line
            sy += tileSize;
            ey += tileSize;
        }

        // vertical lines = only x coordinate is changing
        // set starting and ending position
        sx = startX;
        sy = startY;
        ex = startX;
        ey = startY + gridHeightInPixels;

        // we need to draw width + 1 lines
        for (i = 0; i <= width; i++){
            linesCoordinates[c++] = sx;
            linesCoordinates[c++] = sy;
            linesCoordinates[c++] = ex;
            linesCoordinates[c++] = ey;

            //change x coordinate to move to next line
            sx += tileSize;
            ex += tileSize;
        }
        return linesCoordinates;
    }

    public float[] getGridLinesCoordinates(){
        return gridLinesCoordinates;
    }

    // method converting color from RGB color to int color with full opacity
    // RGB:          R: 124    G:37    B: 215
    // int: 1111111 01111100 00100101 11010111
    public int RGBtoIntColor(int R, int G, int B, int A){
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    public Tile getTile(int index){
        return tiles.get(index);
    }

    public List<Tile> getTiles(){
        return this.tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
