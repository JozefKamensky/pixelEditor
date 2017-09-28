package com.example.jozefkamensky.androidcanvasexample;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by jozefkamensky on 9/24/17.
 */

public class PalleteRecyclerAdapter extends RecyclerView.Adapter<PalleteRecyclerAdapter.ColorHolder> {

    public List<Integer> mColors;

    public PalleteRecyclerAdapter(List<Integer> colors) {
        mColors = colors;
    }

    public static class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //2
        private ImageView mItemImageView;
        private int mColor;

        //4
        public ColorHolder(View v) {
            super(v);
            Log.e("HOLDER", "CREATE!");
            mItemImageView = (ImageView) v.findViewById(R.id.colorPalleteItem);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("HOLDER", "CLICK! color: " + mColor + " adapterPosition: "+ getAdapterPosition());
            //Toast.makeText(v.getContext(), ColorToString(mColor) + ", position: "+ getAdapterPosition(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new ColorChangeEvent(mColor));
        }

        public void bindColor(int color) {
            Log.e("HOLDER", "BIND COLOR "+color);
            mColor = color;
            mItemImageView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        private String ColorToString(int color){
            Log.e("COLOR_CONVERSION", "ColorInt: " + color + ", ColorBinary: " + Integer.toBinaryString(color));
            int a = color >> 24;
            a = (a >> 24) & 0xff;
            int r = color << 8;
            r = (r >> 24) & 0xff;
            int g = color << 16;
            g = (g >> 24) & 0xff;
            int b = color << 24;
            b = (b >> 24) & 0xff;

            return "R: " + r + ", G: " + g + ", B: " + b + ", A: " + a;
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("PALLETE", "onLongClick remove color in position: " + getAdapterPosition());
            EventBus.getDefault().post(new RemoveFromPalleteEvent(getAdapterPosition()));
            return true;
        }
    }

    @Override
    public PalleteRecyclerAdapter.ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_color, parent, false);
        return new ColorHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final PalleteRecyclerAdapter.ColorHolder holder, int position) {
        int color = mColors.get(position);
        holder.bindColor(color);
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    public boolean removeItem(int position){
        int oldSize = mColors.size();
        mColors.remove(position);
        notifyItemRemoved(position);
        return ( oldSize == (mColors.size() + 1));
    }

    public boolean addItem(int color){
        int oldSize = mColors.size();
        mColors.add(color);
        notifyItemInserted(mColors.size());
        return ( oldSize == (mColors.size() - 1));
    }
}
