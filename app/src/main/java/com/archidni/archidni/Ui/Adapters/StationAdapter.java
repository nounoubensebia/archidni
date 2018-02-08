package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 08/02/2018.
 */

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {
    private ArrayList<Station> stations;
    private Coordinate userCoordinate;
    private Context context;

    public StationAdapter(ArrayList<Station> stations, Coordinate userCoordinate, Context context) {
        this.stations = new ArrayList<>(stations);
        this.userCoordinate = userCoordinate;
        this.context = context;
        this.setHasStableIds(true);
        if (userCoordinate!=null)
        {
            TransportUtils.sortStationsByDistance(this.stations,userCoordinate);
        }
    }

    public void updateItems(ArrayList<Station> newItems,Coordinate userCoordinate) {
        ArrayList<Station> stations = new ArrayList<>(newItems);
        TransportUtils.sortStationsByDistance(stations,userCoordinate);
        this.userCoordinate = userCoordinate;
        this.stations.clear();
        this.stations.addAll(stations);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station ,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return stations.get(position).getId();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = stations.get(position);
        holder.mainText.setText(station.getName());
        holder.mainText.setTextColor(ContextCompat.getColor(context,station.getTransportMean().getColor()));
        if (userCoordinate!=null)
        {
            int distance = GeoUtils.distance(userCoordinate,station.getCoordinate());
            holder.distanceText.setText(StringUtils.getTextFromDistance(distance));
            holder.durationText.setText(StringUtils.getTextFromDuration(GeoUtils.getOnFootDuration(
                    (double)distance)));
        }
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,
                station.getTransportMean().getIconEnabled()));
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_name)
        TextView mainText;
        @BindView(R.id.text_distance)
        TextView distanceText;
        @BindView(R.id.text_duration)
        TextView durationText;
        @BindView(R.id.image_transport_mean_icon)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
