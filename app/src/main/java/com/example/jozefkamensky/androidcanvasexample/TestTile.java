package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 5.10.2017.
 */
import android.graphics.RectF;

public class TestTile {
    private boolean colored;
    RectF rect;
    public TestTile(int x, int y, int tileSize){
        colored = false;
        rect = new RectF(x, y, x + tileSize, y + tileSize);
    }

    public boolean isColored() {
        return colored;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    public RectF getRect(){
        return rect;
    }
}
