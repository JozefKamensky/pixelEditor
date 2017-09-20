package com.example.jozefkamensky.androidcanvasexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndroidCanvasExample extends AppCompatActivity {

    private CanvasView customCanvas;
    private Button gridButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        gridButton = (Button) findViewById(R.id.buttonGrid);
    }

    public void showColorPicker(View w){
        Intent intent = new Intent(this, ColorPicker.class);
        startActivity(intent);
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    public void hideGrid(View v) {
        customCanvas.hideGrid();
        gridButton.setText(R.string.showGridButtonTitle);
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGrid(v);
            }
        });
    }

    public void showGrid(View v) {
        customCanvas.showGrid();
        gridButton.setText(R.string.hideGridButtonTitle);
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideGrid(v);
            }
        });
    }
}
