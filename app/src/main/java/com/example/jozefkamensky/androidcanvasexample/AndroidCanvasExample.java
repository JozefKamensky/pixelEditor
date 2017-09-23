package com.example.jozefkamensky.androidcanvasexample;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
        changeSelectedColorVisualization(Settings.getInstance().getColor());
        gridButton = (ImageButton) findViewById(R.id.buttonGrid);
        image = (ImageView) findViewById(R.id.imageViewActualColor);
    }

    public void showColorPicker(View w){
        final Dialog dialog = new Dialog(w.getContext());
        dialog.setContentView(R.layout.color_picker);
        dialog.setTitle(getResources().getString(R.string.colorPickerTitle));
        image = (ImageView) dialog.findViewById(R.id.imageViewActualColor);
        image.getBackground().setColorFilter(Settings.getInstance().getColor(), PorterDuff.Mode.MULTIPLY);
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
                changeSelectedColorVisualization(selectedColor);
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

    public void openSettings(View v){
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.settings);
        dialog.setTitle(R.string.settingsTitle);

        final Spinner width = (Spinner) dialog.findViewById(R.id.spinnerWidthInTiles);
        final Spinner height = (Spinner) dialog.findViewById(R.id.spinnerHeightInTiles);
        final Spinner tileToPx = (Spinner) dialog.findViewById(R.id.spinnerTileToPixel);

        width.setSelection(1);
        height.setSelection(1);
        tileToPx.setSelection(1);

        Button save = (Button) dialog.findViewById(R.id.saveSettingsButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings s = Settings.getInstance();
                int selectedPos;
                int selectedValue;

                selectedPos = width.getSelectedItemPosition();
                String[] size_values = getResources().getStringArray(R.array.settingsGridWidthDimensions);
                selectedValue = Integer.valueOf(size_values[selectedPos]);
                s.setGridWidthInTiles(selectedValue);

                selectedPos = height.getSelectedItemPosition();
                size_values = getResources().getStringArray(R.array.settingsGridHeightDimensions);
                selectedValue = Integer.valueOf(size_values[selectedPos]);
                s.setGridHeightInTiles(selectedValue);

                selectedPos = tileToPx.getSelectedItemPosition();
                size_values = getResources().getStringArray(R.array.settingsTileToPixelConversion);
                selectedValue = Integer.valueOf(size_values[selectedPos]);
                s.setExportPixelsPerTile(selectedValue);
                Toast.makeText(view.getContext(), getResources().getString(R.string.settingsSaveSuccessfulMessage), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                customCanvas.initGrid();
            }
        });

        dialog.show();
    }

    public void exportImage(View v){
        customCanvas.exportImage("test1");
    }

    private void changeSelectedColorVisualization(int color){
        selectedColorButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
