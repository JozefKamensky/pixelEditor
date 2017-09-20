package com.example.jozefkamensky.androidcanvasexample;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */

public class Tile {
    boolean black;
    int x;
    int y;
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
        setWhite();
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(){
        black = true;
    }
    public void setWhite(){
        black = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
