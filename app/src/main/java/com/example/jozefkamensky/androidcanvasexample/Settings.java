package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 21.9.2017.
 */

public class Settings {

    private int color = -12533825;
    private int exportPixelPerTile = 4;

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
}
