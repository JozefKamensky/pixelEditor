package com.example.jozefkamensky.androidcanvasexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jozef.kamensky on 20.9.2017.
 *
 * Class used to store canvas content. It contains all methods that work
 * with canvas or bitmap used for drawing.
 */
public class CanvasView extends View {

    // for performance purposes we use mBitmap to store content of screen
    // so we don`t have to compute and draw all tiles from scratch every frame - we just use same bitmap
    // which is changed only when needed
    private Bitmap mBitmap;
    private Canvas mCanvas;

    // vector drawable used to represent transparent tile
    private static Drawable transparentTile;

    // Paint object used to represent color of rectangle to draw
    private Paint p;

    // lines paint and coordinates
    private Paint linePaint;
    private float[] linesCoordinates;
    // lines can be shown (value true) or hidden (value false)
    private boolean showGrid;

    private Grid grid;

    //color which user use to draw at time
    private int selectedColor;

    // list of indexes of tiles which color was changed and thus they
    // have to be redrawn
    private List<Integer> changes;

    private int screenWidth;
    private int screenHeight;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);

        // set attributes used to draw lines
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.GRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(4f);

        // preselected color
        selectedColor = -12533825;

        // initial settings - we want to see grid, initialize variables
        showGrid = true;
        changes = new ArrayList<>();
        p = new Paint();

        //load drawable for transparent tile
        transparentTile = getResources().getDrawable(R.drawable.transparent_tile_24dp);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        prepareCanvas();
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawChangesToCanvas();
        canvas.drawBitmap(mBitmap, 0, 0, null);
        if (showGrid) canvas.drawLines(linesCoordinates, linePaint);
    }

    // method for drawing background - when grid is created all tiles are set
    // as transparent = so background consists only of drawables for transparent tile
    // this method is computationally expensive and is used only when canvas
    // is created or recreated
    private void drawBasicBackground(Canvas canvas){
        RectF r;
        for (Tile tile : grid.getTiles()){
            r = tile.getRectangle();
            transparentTile.setBounds((int) r.left, (int) r.top, (int) r.right, (int) r.bottom);
            transparentTile.draw(canvas);
        }
    }

    // method to prepare canvas for user
    public void prepareCanvas(){
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        initGrid();
        drawBasicBackground(mCanvas);
        invalidate();
    }

    // method is drawing all changes to mCanvas (changes are stored to mBitmap)
    private void drawChangesToCanvas(){
        // no changes to draw
        if (changes.size() == 0) return;

        // draw all waiting changes
        for (Integer i : changes){
            // get tile that need to be changed (i is index of tile)
            Tile t = grid.getTile(i);

            // if tile is transparent use drawable
            if (t.isTransparent()){
                RectF r = t.getRectangle();
                transparentTile.setBounds( (int) r.left, (int) r.top, (int) r.right, (int) r.bottom);
                transparentTile.draw(mCanvas);
            }
            // if tile is colored draw rectangle
            else {
                p.setColor(t.getColor());
                mCanvas.drawRect(t.getRectangle(), p);
            }
        }

        //all changes are processed so clear list of waiting changes
        changes.clear();
    }

    private void startTouch(float x, float y) {
        selectedColor = Settings.getInstance().getColor();

        // change color of tile at screen coordinates x,y
        int tileChangedIndex = grid.changeTileColor(x, y, selectedColor);

        // if color of tile was changed, add tile index to list of changes - tile
        // has to be redrawn in next frame
        if (tileChangedIndex != -1) changes.add(tileChangedIndex);
    }

    private void moveTouch(float x, float y) {
        // change color of tile at screen coordinates x,y
        int tileChangedIndex = grid.changeTileColor(x, y, selectedColor);

        // if color of tile was changed, add tile index to list of changes - tile
        // has to be redrawn in next frame
        if (tileChangedIndex != -1) changes.add(tileChangedIndex);
    }

    public void clearCanvas() {
        prepareCanvas();
        invalidate();
    }

    public void showGrid() {
        showGrid = true;
        invalidate();
    }

    public void hideGrid() {
        showGrid = false;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("METHOD", "onTouchEvent!");
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
        }
        return true;
    }

    // set up new grid
    private void initGrid(){
        Settings s = Settings.getInstance();
        // get values from setting and initialize new grid
        grid = new Grid(s.getGridWidthInTiles(), s.getGridHeightInTiles(), mCanvas.getWidth(), mCanvas.getHeight());
        // grid was changed so we need to recalculate coordinates of lines
        linesCoordinates = grid.getGridLinesCoordinates();
    }

    public void exportImage(String name){
        Settings s = Settings.getInstance();

        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();
        int pixelsPerTile = s.getExportPixelsPerTile();
        int widthInPixels = gridWidth * pixelsPerTile;
        int heightInPixels = gridHeight * pixelsPerTile;

        Log.d("EXPORT", "fileName: " + name);
        Log.d("EXPORT", "width: " + gridWidth);
        Log.d("EXPORT", "width in pixels: " + widthInPixels);
        Log.d("EXPORT", "height: " + gridHeight);
        Log.d("EXPORT", "height in pixels: " + heightInPixels);
        Log.d("EXPORT", "total pixels: " + (widthInPixels * heightInPixels));

        Bitmap exportBitmap = Bitmap.createBitmap(widthInPixels, heightInPixels, Bitmap.Config.ARGB_8888);
        Log.d("EXPORT", "bitmap height: " + exportBitmap.getHeight());
        Log.d("EXPORT", "bitmap width: " + exportBitmap.getWidth());

        List<Tile> tiles = grid.getTiles();

        for (int y = 0; y < grid.getHeight(); y++){
            for (int x = 0; x < grid.getWidth(); x++){
                for (int i = 0; i < pixelsPerTile; i++){
                    for (int j = 0; j < pixelsPerTile; j++){
                        exportBitmap.setPixel( (y * pixelsPerTile) + i, (x * pixelsPerTile) + j, tiles.get(y * gridWidth + x).getColor() );
                        Log.d("EXPORT", "bitmap x: " + ((x * pixelsPerTile) + j) + ", y: " + ((y * pixelsPerTile) + i) + ", color: " + tiles.get(y * gridWidth + x).getColor());
                    }
                }
            }
        }

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        Log.d("EXPORT", "exportPath: " + path + name + ".png");
        OutputStream fOut;

        // the File to save
        File file = new File(path, name + ".png");
        try {
            fOut = new FileOutputStream(file);
            exportBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            Toast.makeText(getContext(), "Export successful!", Toast.LENGTH_LONG).show();
            Log.d("EXPORT", "Export successful!");
        }
        catch (IOException e){
            Toast.makeText(getContext(), "Export failed!", Toast.LENGTH_LONG).show();
            Log.d("EXPORT", "Export failed!");
            e.printStackTrace();
        }
    }
}
