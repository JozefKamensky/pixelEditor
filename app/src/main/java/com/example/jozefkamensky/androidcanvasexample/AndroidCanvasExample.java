package com.example.jozefkamensky.androidcanvasexample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AndroidCanvasExample extends AppCompatActivity{

    private CanvasView customCanvas;
    private ImageButton gridButton;
    private HSLColorPicker picker;
    private ImageView image;
    private Button selectButton;
    private ImageButton selectedColorButton;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PalleteRecyclerAdapter pAdapter;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.colorList);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        pAdapter = new PalleteRecyclerAdapter(Pallete.getInstance().getColors());
        mRecyclerView.setAdapter(pAdapter);
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
//                selectedColor = color;
                EventBus.getDefault().post(new ColorChangeEvent(color));
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Settings.getInstance().setColor(selectedColor);
//                changeSelectedColorVisualization(selectedColor);
//                EventBus.getDefault().post(new ColorChangeEvent(selectedColor));
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
        final TextView resultImageDimension = (TextView) dialog.findViewById(R.id.resultImageDimensions);

        final String[] width_size_values = getResources().getStringArray(R.array.settingsGridWidthDimensions);
        final String[] height_size_values = getResources().getStringArray(R.array.settingsGridHeightDimensions);
        final String[] conversion_values = getResources().getStringArray(R.array.settingsTileToPixelConversion);

        int width_selected_index = findInArray(width_size_values, String.valueOf(Settings.getInstance().getGridWidthInTiles()));
        int height_selected_index = findInArray(height_size_values, String.valueOf(Settings.getInstance().getGridHeightInTiles()));
        int pixels_selected_index = findInArray(conversion_values, String.valueOf(Settings.getInstance().getExportPixelsPerTile()));

        width.setSelection( (width_selected_index == -1) ? 2 : width_selected_index );
        height.setSelection( (height_selected_index == -1) ? 2 : height_selected_index );
        tileToPx.setSelection( (pixels_selected_index == -1) ? 2 : pixels_selected_index );

        width.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.getInstance().setTmpGridWidthInTiles(Integer.valueOf(width_size_values[width.getSelectedItemPosition()]));
                updateText(resultImageDimension);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.getInstance().setTmpGridHeightInTiles(Integer.valueOf(height_size_values[height.getSelectedItemPosition()]));
                updateText(resultImageDimension);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tileToPx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.getInstance().setTmpExportPixelsPerTile(Integer.valueOf(conversion_values[tileToPx.getSelectedItemPosition()]));
                updateText(resultImageDimension);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button save = (Button) dialog.findViewById(R.id.saveSettingsButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings s = Settings.getInstance();
                int selectedPos;
                int selectedValue;

                selectedPos = width.getSelectedItemPosition();
                selectedValue = Integer.valueOf(width_size_values[selectedPos]);
                s.setGridWidthInTiles(selectedValue);

                selectedPos = height.getSelectedItemPosition();

                selectedValue = Integer.valueOf(height_size_values[selectedPos]);
                s.setGridHeightInTiles(selectedValue);

                selectedPos = tileToPx.getSelectedItemPosition();
                selectedValue = Integer.valueOf(conversion_values[selectedPos]);
                s.setExportPixelsPerTile(selectedValue);
                Toast.makeText(view.getContext(), getResources().getString(R.string.settingsSaveSuccessfulMessage), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                customCanvas.initGrid();
            }
        });

        dialog.show();
    }

    public void exportImage(View v){
        Log.d("EXPORT", "exportImage dialog!");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.exportImageTitle));
        final EditText imageName = new EditText(this);
        imageName.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(imageName);
        builder.setPositiveButton(R.string.exportImageExportButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                customCanvas.exportImage(imageName.getText().toString());
                Log.d("EXPORT", "exportImage button click!");
            }
        });
        builder.setNegativeButton(R.string.exportImageCancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void changeSelectedColorVisualization(int color){
        selectedColorButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ColorChangeEvent event) {
        Settings.getInstance().setColor(event.color);
        changeSelectedColorVisualization(event.color);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void updateText(TextView textView){
        int tmpWidth = Settings.getInstance().getTmpGridWidthInTiles();
        int tmpHeight = Settings.getInstance().getTmpGridHeightInTiles();
        int tmpPixels = Settings.getInstance().getTmpExportPixelsPerTile();
        String message = getResources().getString(R.string.settingsResultImageDimensions);
        message = message.replace("{{0}}", String.valueOf( tmpWidth * tmpPixels));
        message = message.replace("{{1}}", String.valueOf( tmpHeight * tmpPixels));
        textView.setText(message);
    }

    private int findInArray(String[] array, String value){
        for (int i = 0; i < array.length; i++){
            if (array[i].equals(value)){
                return i;
            }
        }
        return -1;
    }
}
