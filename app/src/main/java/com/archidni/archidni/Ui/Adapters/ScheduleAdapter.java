package com.archidni.archidni.Ui.Adapters;

import android.annotation.SuppressLint;
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
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.MetroSchedule;
import com.archidni.archidni.Model.Transport.Schedule.Schedule;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Schedule> schedules;
    private Line line;
    private OnTrainScheduleClick onTrainScheduleClick;

    private static final int TYPE_METRO = 0;
    private static final int TYPE_TRAIN = 1;


    public ScheduleAdapter(Context context, List<Schedule> schedules, Line line, OnTrainScheduleClick onTrainScheduleClick) {
        this.context = context;
        this.schedules = schedules;
        this.line = line;
        this.onTrainScheduleClick = onTrainScheduleClick;
    }

    @Override
    public int getItemViewType(int position) {
        Schedule schedule = schedules.get(position);
        if (schedule instanceof MetroSchedule)
        {
            return TYPE_METRO;
        }
        else
        {
            return TYPE_TRAIN;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == TYPE_METRO)
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_metro_schedule,
                            parent, false);
            return new MetroScheduleViewHolder(v);
        }
        else
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_train_schedule,
                            parent, false);
            return new TrainScheduleViewHolder(v);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MetroScheduleViewHolder)
        {
            MetroSchedule metroSchedule = (MetroSchedule) schedules.get(position);
            MetroScheduleViewHolder viewHolder = (MetroScheduleViewHolder) holder;
            String timePeriodString = String.format("De %s à %s",
                    StringUtils.getTimeString(metroSchedule.getStartTime()),
                    StringUtils.getTimeString(metroSchedule.getEndTime()));
            viewHolder.timePeriodText.setText(timePeriodString);
            ViewUtils.changeTextViewState(context,
                    ((MetroScheduleViewHolder) holder).timePeriodText,
                    line.getTransportMean().getTimesSelectedDrawable(),
                    com.archidni.archidni.UiUtils.ViewUtils.DIRECTION_LEFT);
            String waitingTimeString = String.format("Temps d'attente moyen : %d minutes",
                    metroSchedule.getWaitingTime());
            viewHolder.waitingTimeText.setText(waitingTimeString);
        }
        else
        {
            TrainScheduleViewHolder trainScheduleViewHolder = (TrainScheduleViewHolder) holder;
            final TrainSchedule trainSchedule = (TrainSchedule) schedules.get(position);

            trainScheduleViewHolder.tripText.setText(String.format("Voyage à %s",
                    StringUtils.getTimeString(trainSchedule.getDepartureTime())));
            trainScheduleViewHolder.originText.setText(trainSchedule.getOrigin().getName());
            trainScheduleViewHolder.destinationText.setText(trainSchedule.getDestination().getName());

            ViewUtils.changeTextViewState(context,trainScheduleViewHolder.tripText,
                    trainSchedule.getTransportMean().getTimesSelectedDrawable(),ViewUtils.DIRECTION_LEFT);


            trainScheduleViewHolder.originCircle.setImageDrawable(ContextCompat.getDrawable(context,
                    trainSchedule.getTransportMean().getStationCirleDrawableId()));

            trainScheduleViewHolder.destinationCircle.setImageDrawable(ContextCompat.getDrawable(context,
                    trainSchedule.getTransportMean().getStationCirleDrawableId()));

            trainScheduleViewHolder.arrivalsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTrainScheduleClick.onItemClick(trainSchedule);
                }
            });
            trainScheduleViewHolder.separationView.setBackgroundColor(ContextCompat.getColor(
                    context,trainSchedule.getTransportMean().getColor()
            ));
        }
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    static class MetroScheduleViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_time_period)
        TextView timePeriodText;
        @BindView(R.id.text_waiting_time)
        TextView waitingTimeText;

        public MetroScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    static class TrainScheduleViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_trip)
        TextView tripText;
        @BindView(R.id.text_origin)
        TextView originText;
        @BindView(R.id.text_destination)
        TextView destinationText;
        @BindView(R.id.image_origin_circle)
        ImageView originCircle;
        @BindView(R.id.image_destination_circle)
        ImageView destinationCircle;
        @BindView(R.id.view_separation)
        View separationView;
        @BindView(R.id.layout_station_arrivals)
        View arrivalsLayout;

        public TrainScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnTrainScheduleClick {
        public void onItemClick(TrainSchedule trainSchedule);
    }
}
