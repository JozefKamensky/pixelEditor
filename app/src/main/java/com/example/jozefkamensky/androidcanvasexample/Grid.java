package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    //size of one square tile in pixels
    private int tileSize;

    //dimensions in tiles
    private int width;
    private int height;

    private int startX;
    private int startY;

    private List<TestTile> tempTiles;
    private SparseArray<List<TestTile>> tiles;
    private float[] gridLinesCoordinates;

    public Grid(int widthInTiles, int heightInTiles, int viewWidth, int viewHeight){
        int minPaddingHorizontal = 50;
        int minPaddingVertical = 100;

        this.width = widthInTiles;
        this.height = heightInTiles;

        //calculate maximum tileSize in pixels to fit grid on screen
        int tileSize1 = (viewWidth - 2 * minPaddingHorizontal) / widthInTiles;
        int tileSize2 = (viewHeight - 2 * minPaddingVertical) / heightInTiles;
        this.tileSize = (tileSize1 < tileSize2) ? tileSize1 : tileSize2;

        startX = (viewWidth - widthInTiles * tileSize) / 2;
        startY = (viewHeight - heightInTiles * tileSize) / 2;

        tempTiles = new ArrayList<>();
        tiles = new SparseArray<>();

        int transparentWhite = RGBtoIntColor(255,255,255, 0);
        for (int i = 0; i < heightInTiles; i++){
            for (int j = 0; j < widthInTiles; j++){
                TestTile t = new TestTile(j, i, tileSize,
                        startX + j * tileSize, startY + i * tileSize , transparentWhite);
                tempTiles.add(t);
            }
        }
        tiles.append(transparentWhite, tempTiles);
        gridLinesCoordinates = calculateGridLinesCoordinates();
    }

    public void changeTileColor(float x, float y, int newColor){

        Log.d("GRID", "changeTileColor!");
        int tileX = ((int)x - startX) / tileSize;
        int tileY = ((int)y - startY) / tileSize;
        if (tileX < 0 || tileX >= width) return;
        if (tileY < 0 || tileY >= height) return;
        Log.d("GRID", "grid x: " + tileX + ", grid y: " + tileY );
        Log.d("GRID", "screen x: " + x + ", screen y: " + y );
        TestTile t = findTile(tileX, tileY);
        if (t.getColor() != newColor) {
            Log.d("GRID", "removeTileFromList for color: " + t.getColor());
            removeTileFromList(tileX, tileY, tiles.get(t.getColor()));
            t.setColor(newColor);
            addTile(t, newColor);
        }
    }

    private void removeTileFromList(int x, int y, List<TestTile> tilesForColor){
        for(int j = 0; j < tilesForColor.size(); j++){
            TestTile t = tilesForColor.get(j);
            if ((t.getX() == x) && (t.getY() == y)){
                Log.d("GRID", "tile found and removed" );
                tilesForColor.remove(j);
                return;
            }
        }
    }

    private void addTile(TestTile t, int color){
        Log.d("GRID", "addTile for color: " + color);
        if (tiles.get(color) == null) {
            Log.d("GRID", "color used for the first time, creating new storage.");
            tiles.append(color, new ArrayList<TestTile>());
            tiles.get(color).add(t);
        }
        else tiles.get(color).add(t);
    }

    private TestTile findTile(int x, int y){
        TestTile t = tempTiles.get(y * width + x);
        Log.d("GRID", "found tile for x: " + x + " and y: " + y + " at index " + (y * width + x));
        Log.d("GRID", "tile x: " + t.getX() + ", y: " + t.getY());
        return t;
    }

    private float[] calculateGridLinesCoordinates(){
        //calculate position of grid lines and store them
        int gridWidthInPixels = width * tileSize - 1;
        int gridHeightInPixels = height * tileSize - 1;
        int i;

        float[] linesCoordinates = new float[4 * (width + height + 2)];
        int c = 0;

        //add horizontal lines
        float sx = startX;
        float sy = startY;
        float ex = startX + gridWidthInPixels;
        float ey = startY;
        for (i = 0; i <= height; i++){
            linesCoordinates[c++] = sx;
            linesCoordinates[c++] = sy;
            linesCoordinates[c++] = ex;
            linesCoordinates[c++] = ey;

            sy += tileSize;
            ey += tileSize;
        }

        //add vertical lines
        sx = startX;
        sy = startY;
        ex = startX;
        ey = startY + gridHeightInPixels;
        for (i = 0; i <= width; i++){
            linesCoordinates[c++] = sx;
            linesCoordinates[c++] = sy;
            linesCoordinates[c++] = ex;
            linesCoordinates[c++] = ey;

            sx += tileSize;
            ex += tileSize;
        }
        return linesCoordinates;
    }

    public float[] getGridLinesCoordinates(){
        return gridLinesCoordinates;
    }

    public int RGBtoIntColor(int R, int G, int B, int A){
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    public SparseArray<List<TestTile>> getTiles(){
        return tiles;
    }
}
