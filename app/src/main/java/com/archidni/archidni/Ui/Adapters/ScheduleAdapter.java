package com.archidni.archidni.Ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.MetroSchedule;
import com.archidni.archidni.Model.Transport.Schedule;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Schedule> schedules;
    private Line line;

    private static final int TYPE_METRO = 0;
    private static final int TYPE_TRAIN = 1;

    public ScheduleAdapter(Context context, List<Schedule> schedules, Line line) {
        this.context = context;
        this.schedules = schedules;
        this.line = line;
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
            String timePeriodString = String.format("%s - %s",
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

        public TrainScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
