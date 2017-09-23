package com.example.jozefkamensky.androidcanvasexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */
public class CanvasView extends View {

    Context context;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint linePaint;

    private Grid grid;
    private float[] gridCoordinates;
    private boolean showGrid;
    private int widthInTiles = 16;
    private int heightInTiles = 16;
    private Settings settings;
    private int selectedColor;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // and we set a new Paint with the desired attributes
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(4f);
        selectedColor = -12533825;
        showGrid = true;
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        initGrid();

//        int bitmapWidth = mBitmap.getWidth();
//        int bitmapHeight = mBitmap.getHeight();
//        int bitmapRowBytes = mBitmap.getRowBytes();
//        int[] pixels = new int[bitmapWidth * bitmapHeight];
//        Log.i("BITMAP", "bitmap width: " + bitmapWidth);
//        Log.i("BITMAP", "bitmap height: " + bitmapHeight);
//        Log.i("BITMAP", "bitmap row bytes: " + bitmapRowBytes);
//        mBitmap.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
//        Log.i("BITMAP", "pixels count: " + pixels.length);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGridContent(canvas);
        if (showGrid) canvas.drawLines(gridCoordinates, linePaint);

    }

    private void drawGridContent(Canvas canvas) {
        for(Tile t: grid.getGridTilesAsList()){
            canvas.drawRect(t.getTileAsRect(grid.getTileSize()), t.getPaint());
        }
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        selectedColor = Settings.getInstance().getColor();
        grid.changeTileColor(x, y, selectedColor);

    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        grid.changeTileColor(x, y, selectedColor);
    }

    public void clearCanvas() {
        initGrid();
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

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    public void initGrid(){
        Settings s = Settings.getInstance();
        grid = new Grid(s.getGridWidthInTiles(), s.getGridHeightInTiles(), mCanvas.getWidth(), mCanvas.getHeight());
        gridCoordinates = grid.getGridLinesCoordinates();
        invalidate();
    }

    public void exportImage(String name){
        Settings s = Settings.getInstance();

        int pixelsPerTile = s.getExportPixelsPerTile();
        int widthInPixels = grid.getWidth() * pixelsPerTile;
        int heigthInPixels = grid.getHeight() * pixelsPerTile;

        Log.e("EXPORT", "fileName: " + name);
        Log.e("EXPORT", "width: " + grid.getWidth());
        Log.e("EXPORT", "width in pixels: " + widthInPixels);
        Log.e("EXPORT", "height: " + grid.getHeight());
        Log.e("EXPORT", "height in pixels: " + heigthInPixels);
        Log.e("EXPORT", "total pixels: " + (widthInPixels * heigthInPixels));

        Bitmap exportBitmap = Bitmap.createBitmap(widthInPixels, heigthInPixels, Bitmap.Config.ARGB_8888);
        Log.e("EXPORT", "bitmap height: " + exportBitmap.getHeight());
        Log.e("EXPORT", "bitmap width: " + exportBitmap.getWidth());

        Tile[][] tiles = grid.getGridTiles();
        for (int y = 0; y < heigthInPixels; y += pixelsPerTile){
            for (int x = 0; x < widthInPixels; x += pixelsPerTile){
                for (int i = 0; i < pixelsPerTile; i++){
                    for (int j = 0; j < pixelsPerTile; j++){
                        exportBitmap.setPixel(y + i, x + j, tiles[x][y].getPaint().getColor());
                    }
                }
            }
        }

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        Log.e("EXPORT", "exportPath: " + path);
        OutputStream fOut = null;

        // the File to save
        File file = new File(path, name+".png");
        try {
            fOut = new FileOutputStream(file);

            exportBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
        }
        catch (IOException e){
            Toast.makeText(getContext(), "Export failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
}
