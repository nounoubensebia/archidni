package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nouno on 15/02/2018.
 */

public class LineStationSuggestionAdapter extends RecyclerView.Adapter<LineStationSuggestionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LineStationSuggestion> lineStationSuggestions;
    private LineStationSuggestionAdapter.OnItemClickedListener onItemClickListener;

    public LineStationSuggestionAdapter(Context context, ArrayList<LineStationSuggestion> lineStationSuggestions, OnItemClickedListener onItemClickListener) {
        this.context = context;
        this.lineStationSuggestions = lineStationSuggestions;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_station_suggestion,
                parent, false);
        return new ViewHolder(v);
    }

    public void updateItems (ArrayList<LineStationSuggestion> lineStationSuggestions1)
    {
        lineStationSuggestions = lineStationSuggestions1;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LineStationSuggestion lineStationSuggestion = lineStationSuggestions.get(position);
        holder.nameText.setText(lineStationSuggestion.getName());
        holder.transportMeanIcon.setImageDrawable(ContextCompat.getDrawable(context,
                lineStationSuggestion.getTransportMean().getIconEnabled()));
        //holder.nameText.setTextColor(ContextCompat.getColor(context,
        //        lineStationSuggestion.getTransportMean().getColor()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(lineStationSuggestion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lineStationSuggestions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.image_transport_mean_icon)
        ImageView transportMeanIcon;
        @BindView(R.id.container)
        View container;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked (LineStationSuggestion lineStationSuggestion);
    }
}
