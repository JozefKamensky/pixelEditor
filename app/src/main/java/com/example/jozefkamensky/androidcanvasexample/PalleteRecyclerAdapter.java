package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jozefkamensky on 9/24/17.
 */

public class PalleteRecyclerAdapter extends RecyclerView.Adapter<PalleteRecyclerAdapter.ColorHolder> {

    private List<Integer> mColors;

    public PalleteRecyclerAdapter(List<Integer> colors) {
        mColors = colors;
    }

    public static class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private ImageView mItemImageView;
        private int mColor;

        private static final String COLOR_KEY = "COLOR";

        //4
        public ColorHolder(View v) {
            super(v);
            Log.e("HOLDER", "CREATE!");
            mItemImageView = (ImageView) v.findViewById(R.id.colorPalleteItem);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("HOLDER", "CLICK! color: " + mColor + " adapterPosition: "+ getAdapterPosition());
        }

        public void bindColor(int color) {
            Log.e("HOLDER", "BIND COLOR "+color);
            mColor = color;
            mItemImageView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public PalleteRecyclerAdapter.ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_color, parent, false);
        return new ColorHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PalleteRecyclerAdapter.ColorHolder holder, int position) {
        int color = mColors.get(position);
        holder.bindColor(color);
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }
}
