package com.example.jozefkamensky.androidcanvasexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jozefkamensky on 9/24/17.
 */

public class Pallete {
    private static final Pallete ourInstance = new Pallete();
    private List<Integer> colors;

    public static Pallete getInstance() {
        return ourInstance;
    }

    private Pallete() {
        colors = new ArrayList<>();
        addRGBAColor(255,255,255,255);
        addRGBAColor(0,0,0,255);

        addRGBAColor(255,0,0,255);
        addRGBAColor(0,255,0,255);
        addRGBAColor(0,0,255,255);

        addRGBAColor(255,255,0,255);
        addRGBAColor(255,0,255,255);
        addRGBAColor(0,255,255,255);
    }

    private int convertColorFromRGBAToInt(int R, int G, int B, int A){
        int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
        return color;
    }

    public void addIntColor(int color){
        colors.add(color);
    }

    public void addRGBAColor(int R, int G, int B, int A){
        colors.add(convertColorFromRGBAToInt(R,G,B,A));
    }

    public List<Integer> getColors(){
        return Collections.unmodifiableList(colors);
    }
}
