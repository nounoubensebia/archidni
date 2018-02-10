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
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TrainLine;
import com.archidni.archidni.Model.Transport.TrainTrip;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 10/02/2018.
 */

public class TrainTripAdapter extends RecyclerView.Adapter<TrainTripAdapter.ViewHolder> {
    private Context context;
    private long departureTime;
    private long departureDate;
    private Station station;
    private ArrayList<Line> trainLines;
    private static final int MAX_TO_GET = 5;

    public TrainTripAdapter(Context context, long departureTime, long departureDate, Station station,
                            ArrayList<Line> trainLines) {
        this.context = context;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.station = station;
        this.trainLines = trainLines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_trip,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<Pair<Line,TrainTrip>,Long> pair = TransportUtils.getNextDeparturesFromStation(trainLines
                ,station,departureTime,departureDate,MAX_TO_GET).get(position);
        Line line = pair.first.first;
        TrainTrip trainTrip = pair.first.second;
        long remainingTime = pair.second;
        holder.nameText.setText(line.getName()+" (Vers "+trainTrip.getDestination(line).getName()+")");
        holder.originText.setText(trainTrip.getOrigin(line).getName());
        holder.destinationText.setText(trainTrip.getDestination(line).getName());
        holder.timeText.setText(StringUtils.getTimeString(remainingTime)+
                " (dans "+StringUtils.getTextFromDuration(remainingTime-departureTime)+")");
        holder.transportMeanIcon.setImageDrawable(ContextCompat.getDrawable(context,
                line.getTransportMean().getIconEnabled()));
        holder.nameText.setTextColor(ContextCompat.getColor(context,line.getTransportMean()
                .getColor()));
        ViewUtils.changeTextViewState(context,holder.originText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
        ViewUtils.changeTextViewState(context,holder.destinationText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
    }

    @Override
    public int getItemCount() {
        return TransportUtils.getNextDeparturesFromStation(trainLines,station,departureTime,
                departureDate,MAX_TO_GET).size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.text_origin)
        TextView originText;
        @BindView(R.id.text_destination)
        TextView destinationText;
        @BindView(R.id.text_time)
        TextView timeText;
        @BindView(R.id.image_transport_mean_icon)
        ImageView transportMeanIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
