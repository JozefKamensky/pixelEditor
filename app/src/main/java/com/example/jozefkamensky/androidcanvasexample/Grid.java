package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

public class Grid {

    //size of one square tile in pixels
    private int tileSize;

    //dimensions in tiles
    private int width;
    private int height;

    private Tile[][] tiles;

    private int startX;
    private int startY;

    public Grid(int widthInTiles, int heightInTiles, int viewWidth, int viewHeight){
        int minPaddingHorizontal = 50;
        int minPaddingVertical = 100;

        this.width = widthInTiles;
        this.height = heightInTiles;
        Log.e("GRID_CONSTRUCTOR", "Grid width: " + width);
        Log.e("GRID_CONSTRUCTOR", "Grid height: " + height);

        int tileSize1 = (viewWidth - 2 * minPaddingHorizontal) / widthInTiles;
        int tileSize2 = (viewHeight - 2 * minPaddingVertical) / heightInTiles;
        this.tileSize = (tileSize1 < tileSize2) ? tileSize1 : tileSize2;
        Log.e("GRID_CONSTRUCTOR", "Grid tile size: " + tileSize);

        startX = (viewWidth - widthInTiles * tileSize) / 2;
        startY = (viewHeight - heightInTiles * tileSize) / 2;

        tiles = new Tile[heightInTiles][widthInTiles];
        for (int i = 0; i < heightInTiles; i++){
            for (int j = 0; j < widthInTiles; j++){
                tiles[i][j] = new Tile(startX + j * tileSize, startY + i * tileSize);
            }
        }
    }

    public void changeTileColor(float x, float y){

        int tileX = ((int)x - startX) / tileSize;
        int tileY = ((int)y - startY) / tileSize;
        if (tileX < 0 || tileX >= width) return;
        if (tileY < 0 || tileY >= height) return;
        tiles[tileY][tileX].black = !tiles[tileY][tileX].black;
    }

    public float[] getGridLinesCoordinates(){
        int gridWidthInPixels = width * tileSize - 1;
        int gridHeightInPixels = height * tileSize - 1;
        int i;

        float[] res = new float[4 * (width + height + 2)];
        int c = 0;

        //add horizontal lines
        float sx = startX;
        float sy = startY;
        float ex = startX + gridWidthInPixels;
        float ey = startY;
        for (i = 0; i <= height; i++){
            res[c++] = sx;
            res[c++] = sy;
            res[c++] = ex;
            res[c++] = ey;

            sy += tileSize;
            ey += tileSize;
        }

        //add vertical lines
        sx = startX;
        sy = startY;
        ex = startX;
        ey = startY + gridHeightInPixels;
        for (i = 0; i <= width; i++){
            res[c++] = sx;
            res[c++] = sy;
            res[c++] = ex;
            res[c++] = ey;

            sx += tileSize;
            ex += tileSize;
        }

        return res;
    }

    public List<GridTileToDraw> getGridTiles(){

        List<GridTileToDraw> res = new ArrayList<>();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                GridTileToDraw tile = new GridTileToDraw();
                if (tiles[i][j].isBlack()) tile.setColor(0,0,0,255);
                else tile.setColor(255,255,255,0);

                int x = tiles[i][j].getX();
                int y = tiles[i][j].getY();
                tile.setRect(new RectF(x,y, x + tileSize, y + tileSize));

                res.add(tile);
            }
        }
        return res;
    }

}
