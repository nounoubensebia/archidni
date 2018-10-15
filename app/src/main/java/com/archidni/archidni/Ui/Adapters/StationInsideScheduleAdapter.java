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

import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationTime;
import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationInsideScheduleAdapter extends RecyclerView.Adapter<StationInsideScheduleAdapter.ViewHolder> {

    private Context context;
    private TrainSchedule trainSchedule;
    private OnItemClick onItemClick;

    public StationInsideScheduleAdapter(Context context, TrainSchedule trainSchedule, OnItemClick onItemClick) {
        this.context = context;
        this.trainSchedule = trainSchedule;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station_inside_schedule,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Station station = trainSchedule.getStationTimes().get(position).getStation();
        holder.stationNameText.setText(station.getName());
        holder.circleImage.setImageDrawable(ContextCompat.getDrawable(context,station.getTransportMean().getStationCirleDrawableId()));
        holder.separationTransportMode.setBackgroundColor(ContextCompat.getColor(context,station.getTransportMean().getColor()));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(trainSchedule.getStationTimes().get(position));
            }
        });
        if (position == getItemCount()-1)
        {
            holder.separationTransportMode.setVisibility(View.GONE);
        }
        else
        {
            holder.separationTransportMode.setVisibility(View.VISIBLE);
        }
        holder.arrivalTime.setText(StringUtils.getTimeString(trainSchedule.getDepartureTime()
                +trainSchedule.getStationTimes().get(position).getTimeAtStation()*60));
    }

    @Override
    public int getItemCount() {
        return trainSchedule.getStationTimes().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.root)
        View root;
        @BindView(R.id.text_name)
        TextView stationNameText;
        @BindView(R.id.text_arrival_time)
        TextView arrivalTime;
        @BindView(R.id.view_separation_transport_mean)
        View separationTransportMode;
        @BindView(R.id.layout_instruction1)
        ImageView circleImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick {
        void onItemClick (StationTime stationTime);
    }
}
