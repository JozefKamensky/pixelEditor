package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 21.9.2017.
 */

public class Settings {

    private int color = -12533825;
    private int exportPixelsPerTile = 4;
    private int gridWidthInTiles = 8;
    private int gridHeightInTiles = 8;

    private static final Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
    }

    public void setColorRGBA(int R, int G, int B, int A) {
        color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 16 | (B & 0xff);
    }

    public int getColor(){
        return color;
    }

    public void setColor(int clr){
        color = clr;
    }

    public int getExportPixelsPerTile() {
        return exportPixelsPerTile;
    }

    public void setExportPixelsPerTile(int exportPixelsPerTile) {
        this.exportPixelsPerTile = exportPixelsPerTile;
    }

    public int getGridWidthInTiles() {
        return gridWidthInTiles;
    }

    public void setGridWidthInTiles(int gridWidthInTiles) {
        this.gridWidthInTiles = gridWidthInTiles;
    }

    public int getGridHeightInTiles() {
        return gridHeightInTiles;
    }

    public void setGridHeightInTiles(int gridHeightInTiles) {
        this.gridHeightInTiles = gridHeightInTiles;
    }
}
