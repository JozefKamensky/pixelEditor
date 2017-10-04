package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.Paint;
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
    private int[][] tilesInt;

    private int startX;
    private int startY;

    public Grid(int widthInTiles, int heightInTiles, int viewWidth, int viewHeight){
        int minPaddingHorizontal = 50;
        int minPaddingVertical = 100;

        this.width = widthInTiles;
        this.height = heightInTiles;
        Log.d("GRID_CONSTRUCTOR", "Grid width: " + width);
        Log.d("GRID_CONSTRUCTOR", "Grid height: " + height);

        int tileSize1 = (viewWidth - 2 * minPaddingHorizontal) / widthInTiles;
        int tileSize2 = (viewHeight - 2 * minPaddingVertical) / heightInTiles;
        this.tileSize = (tileSize1 < tileSize2) ? tileSize1 : tileSize2;
        Log.d("GRID_CONSTRUCTOR", "Grid tile size: " + tileSize);

        startX = (viewWidth - widthInTiles * tileSize) / 2;
        startY = (viewHeight - heightInTiles * tileSize) / 2;

        tiles = new Tile[heightInTiles][widthInTiles];
        tilesInt = new int[heightInTiles][widthInTiles];

        //int transparentWhite = RGBtoIntColor(255,255,255, 0);
        int color = RGBtoIntColor(255,255,255,0);
        for (int i = 0; i < heightInTiles; i++){
            for (int j = 0; j < widthInTiles; j++){
                tiles[i][j] = new Tile(startX + j * tileSize, startY + i * tileSize);
                //tilesInt[i][j] = color;
            }
        }
    }

    public void changeTileColor(float x, float y, int color){

        int tileX = ((int)x - startX) / tileSize;
        int tileY = ((int)y - startY) / tileSize;
        if (tileX < 0 || tileX >= width) return;
        if (tileY < 0 || tileY >= height) return;
        tiles[tileY][tileX].setPaint(color);
        tilesInt[tileY][tileX] = color;
        Log.d("TILES", "tile (" + tileX + ", " + tileY + "), color: " + tilesInt[tileY][tileX]);
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

    public List<Tile> getGridTilesAsList(){

        List<Tile> res = new ArrayList<>();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                res.add(tiles[i][j]);
            }
        }
        return res;
    }

    public List<RectFToDraw> getRectanglesToDraw(){
        List<RectFToDraw> result = new ArrayList<>();
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (isColorTransparent(tiles[i][j].getPaint().getColor())){
                    result.addAll(divideTileToRects(tiles[i][j].getX(), tiles[i][j].getY(), tileSize));
                }
                else result.add(new RectFToDraw(tiles[i][j].getTileAsRect(tileSize), tiles[i][j].getPaint()));
            }
        }
        return result;
    }

    public Tile[][] getGridTiles(){
        Log.d("TILE", "GRID - top left corner color: " + tiles[0][0].getPaint().getColor());
        return tiles;
    }

    public int[][] getGridTilesInt(){
        return tilesInt;
    }

    public int getColorOfTile(int x, int y){
        return tilesInt[y][x];
    }

    public int getTileSize(){
        return tileSize;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int RGBtoIntColor(int R, int G, int B, int A){
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    private boolean isColorTransparent(int color){
        int a = (color >> 24) & 0xff;
        return (a == 0);
    }

    public void shiftToRight(){
        int y;
        for (y = 0; y < height; y++){
            for (int x = width - 1; x > 0 ; x--){
                tiles[y][x].setPaint(tiles[y][x - 1].getPaint().getColor());
            }
        }
        for (y = 0; y < height; y++) tiles[y][0].setColor(255,255,255,0);
    }

    public void shiftToLeft(){
        int y;
        for (y = 0; y < height; y++){
            for (int x = 0; x < width - 1 ; x++){
                tiles[y][x].setPaint(tiles[y][x + 1].getPaint().getColor());
            }
        }
        for (y = 0; y < height; y++) tiles[y][width - 1].setColor(255,255,255,0);
    }

    private List<RectFToDraw> divideTileToRects(int x, int y, int tileSize){
        List<RectFToDraw> rectangles = new ArrayList<>();
        int edge = tileSize/2;

        Paint white = new Paint();
        white.setColor(convertColorFromRGBAToInt(255,255,255,255));

        Paint grey = new Paint();
        grey.setColor(convertColorFromRGBAToInt(101,108,119,255));

        boolean whiteTurn = false;
        for (int i = 0; i < 2; i++){
            whiteTurn = !whiteTurn;
            for (int j = 0; j < 2; j++){
                if (whiteTurn) {
                    rectangles.add(new RectFToDraw(new RectF(x + (edge * i), y + (edge * j), x + (edge * (i + 1)), y + (edge * (j + 1))), white));
                }
                else {
                    rectangles.add(new RectFToDraw(new RectF(x + (edge * i), y + (edge * j), x + (edge * (i + 1)), y + (edge * (j + 1))), grey));
                }
                whiteTurn = !whiteTurn;
            }
        }

        return rectangles;
    }

    private int convertColorFromRGBAToInt(int R, int G, int B, int A){
        int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
        return color;
    }
}
