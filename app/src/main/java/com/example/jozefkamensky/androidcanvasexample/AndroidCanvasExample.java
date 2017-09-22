package com.example.jozefkamensky.androidcanvasexample;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class AndroidCanvasExample extends AppCompatActivity {

    private CanvasView customCanvas;
    private ImageButton gridButton;
    private HSLColorPicker picker;
    private ImageView image;
    private Button selectButton;
    private ImageButton selectedColorButton;
    private int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        selectedColorButton = (ImageButton) findViewById(R.id.buttonSelectedColor);
        gridButton = (ImageButton) findViewById(R.id.buttonGrid);
        image = (ImageView) findViewById(R.id.imageViewActualColor);
    }

    public void showColorPicker(View w){
        final Dialog dialog = new Dialog(w.getContext());
        dialog.setContentView(R.layout.color_picker);
        dialog.setTitle(getResources().getString(R.string.colorPickerTitle));
        image = (ImageView) dialog.findViewById(R.id.imageViewActualColor);
        picker = (HSLColorPicker) dialog.findViewById(R.id.HSLColorPicker);
        selectButton = (Button) dialog.findViewById(R.id.buttonSelectColor);

        picker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                // Do whatever you want with the color
                image.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                selectedColor = color;
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getInstance().setColor(selectedColor);
                selectedColorButton.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    public void hideGrid(View v) {
        customCanvas.hideGrid();
        gridButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_off_24dp));
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGrid(v);
            }
        });
    }

    public void showGrid(View v) {
        customCanvas.showGrid();
        gridButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_on_24dp));
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideGrid(v);
            }
        });
    }
}
