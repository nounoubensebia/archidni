package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationInsideRideInstructionAdapter extends RecyclerView.Adapter<StationInsideRideInstructionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Station> stations;

    public StationInsideRideInstructionAdapter(Context context, ArrayList<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station_inside_line,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.labelText.setText(station.getName());
        holder.instructionImage.setImageDrawable(ContextCompat.getDrawable(context,station.getTransportMean().getStationCirleDrawableId()));
        holder.separationView.setBackgroundColor(ContextCompat.getColor(context,station.getTransportMean().getColor()));

    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_instruction1)
        ImageView instructionImage;
        @BindView(R.id.text_label)
        TextView labelText;
        @BindView(R.id.view_separation_transport_mean1)
        View separationView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
