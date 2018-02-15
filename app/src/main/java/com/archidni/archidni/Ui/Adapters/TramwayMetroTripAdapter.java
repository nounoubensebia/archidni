package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.TimePeriod;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 10/02/2018.
 */

public class TramwayMetroTripAdapter extends RecyclerView.Adapter<TramwayMetroTripAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Line> lines;
    private long departureDate;

    public TramwayMetroTripAdapter(Context context, ArrayList<Line> lines, long departureDate) {
        this.context = context;
        this.lines = lines;
        this.departureDate = departureDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_metro_tramway_trip,
                parent, false);
        return new TramwayMetroTripAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<Pair<Line,TimePeriod>> pairs = TransportUtils.getDayTrips(departureDate,
                lines);
        Line line = pairs.get(position).first;
        TimePeriod timePeriod = pairs.get(position).second;
        holder.nameText.setText(line.getName());
        holder.originText.setText(line.getOrigin().getName());
        holder.destinationText.setText(line.getDestination().getName());
        String timePeriodString = "De " + StringUtils.getTimeString(timePeriod.getStart())+" à "+
                StringUtils.getTimeString(timePeriod.getEnd());
        holder.timePeriodText.setText(timePeriodString);
        holder.waitingTimeText.setText("Temps d'attente : "+
                timePeriod.getAverageWaitingTime()+ " minutes");
        holder.transportMeanImage.setImageDrawable(ContextCompat.getDrawable(context,
                line.getTransportMean().getIconEnabled()));
        ViewUtils.changeTextViewState(context,holder.originText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
        ViewUtils.changeTextViewState(context,holder.destinationText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
        holder.nameText.setTextColor(ContextCompat.getColor(context,line.getTransportMean().getColor()));
    }

    @Override
    public int getItemCount() {
        return TransportUtils.getDayTrips(departureDate,
                lines).size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.text_origin)
        TextView originText;
        @BindView(R.id.text_destination)
        TextView destinationText;
        @BindView(R.id.text_time_period)
        TextView timePeriodText;
        @BindView(R.id.text_waiting_time)
        TextView waitingTimeText;
        @BindView(R.id.image_transport_mean_icon)
        ImageView transportMeanImage;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
