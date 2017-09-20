package com.example.jozefkamensky.androidcanvasexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by jozef.kamensky on 20.9.2017.
 */
public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint linePaint;
    private Paint rectPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private static final float squareEdge = 40;
    private Random rng = new Random();

    private Grid grid;
    private float[] gridCoordinates;
    private boolean showGrid;
    private int widthInTiles = 16;
    private int heightInTiles = 16;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(4f);

        rectPaint = new Paint();
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.FILL);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        initGrid();
        gridCoordinates = grid.getGridLinesCoordinates();
        showGrid = true;
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGridContent(canvas);
        if (showGrid) canvas.drawLines(gridCoordinates, linePaint);

    }

    private void drawGridContent(Canvas canvas) {
        for(Tile t: grid.getGridTiles()){
            canvas.drawRect(t.getTileAsRect(), t.getColor());
        }
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        grid.changeTileColor(x, y, rectPaint);

    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        grid.changeTileColor(x, y, rectPaint);
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

    private void initGrid(){
        grid = new Grid(widthInTiles, heightInTiles, mCanvas.getWidth(), mCanvas.getHeight());
    }
}
