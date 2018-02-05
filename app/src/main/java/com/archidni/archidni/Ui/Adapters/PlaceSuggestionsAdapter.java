package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 03/02/2018.
 */

public class PlaceSuggestionsAdapter extends RecyclerView.Adapter<PlaceSuggestionsAdapter.ViewHolder> {

    private ArrayList<PlaceSuggestion> placeSuggestions;
    private OnItemClickListener onItemClickedListener;
    private Context context;

    public PlaceSuggestionsAdapter(ArrayList<PlaceSuggestion> placeSuggestions,
                                   OnItemClickListener onItemClickedListener,
                                   Context context) {
        this.placeSuggestions = placeSuggestions;
        this.onItemClickedListener = onItemClickedListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_suggestion ,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final PlaceSuggestion placeSuggestion = placeSuggestions.get(position);
        viewHolder.mainText.setText(placeSuggestions.get(position).getMainText());
        viewHolder.secondaryText.setText(placeSuggestions.get(position).getSecondaryText());
        viewHolder.containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListener.onItemClick(placeSuggestion);
            }
        });
        Drawable drawable = ContextCompat.getDrawable(context,placeSuggestion.getDrawable());
        viewHolder.icon.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return placeSuggestions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_main)
        TextView mainText;
        @BindView(R.id.text_secondary)
        TextView secondaryText;
        @BindView(R.id.container)
        View containter;
        @BindView(R.id.image)
        ImageView icon;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PlaceSuggestion placeSuggestion);
    }
}
