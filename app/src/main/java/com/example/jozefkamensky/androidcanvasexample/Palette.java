package com.example.jozefkamensky.androidcanvasexample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jozefkamensky on 9/24/17.
 * Class contains palette of available colors used for quick choosing color.
 * All colors of palette are stored in file and thus all changes (add or remove)
 * are persistent. Palette is initialized with colors of ENDESGA 16 color list
 * (https://lospec.com/palette-list/endesga16).
 */

public class Palette {
    private static final Palette ourInstance = new Palette();
    private List<Integer> colors;

    public static Palette getInstance() {
        return ourInstance;
    }

    private Palette() {
        colors = new ArrayList<>();
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
        return new ArrayList<>(colors);
    }

    public void setColors(List<Integer> clrs){
        colors = clrs;
    }

    public void initializePallete(){
        addRGBAColor(255, 255, 255, 0);
        addRGBAColor(228, 166, 114, 255);
        addRGBAColor(184, 111, 80, 255);
        addRGBAColor(116, 63, 57, 255);
        addRGBAColor(63, 40, 50, 255);

        addRGBAColor(158,40,53, 255);
        addRGBAColor(229,59,68, 255);
        addRGBAColor(251,146,43, 255);
        addRGBAColor(255,231,98, 255);

        addRGBAColor(99,198,77, 255);
        addRGBAColor(50,115,69, 255);
        addRGBAColor(25,61,63, 255);
        addRGBAColor(79,103,129, 255);

        addRGBAColor(175,191,210, 255);
        addRGBAColor(255, 255, 255, 255);
        addRGBAColor(44,232,244, 255);
        addRGBAColor(4,132,209, 255);

    }

    public void removeColor(int index){
        colors.remove(index);
    }
}
