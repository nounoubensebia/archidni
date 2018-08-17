package com.archidni.archidni.Ui.Adapters;

/**
 * Created by nouno on 22/08/2017.
 */






import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by nouno on 07/07/2017.
 */

public class StationInsideLineAdapter extends ArrayAdapter<Station> {
    private Station selectedStation=null;
    private boolean addSeparationViewToLastItem;

    public StationInsideLineAdapter(Context context, ArrayList<Station> stations, Station selectedStation,boolean addSeparationViewToLastItem)
    {
        super(context,0,stations);
        this.selectedStation = selectedStation;
        this.addSeparationViewToLastItem = addSeparationViewToLastItem;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        Station stationTripUi = getItem(position);
        if (item ==null)
        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_station_inside_line,parent, false);

        }
        TextView StationLabelText = item.findViewById(R.id.text_label);

        StationLabelText.setText(stationTripUi.getName());


        ImageView button = item.findViewById(R.id.layout_instruction1);
        button.setImageDrawable(ContextCompat.getDrawable(getContext(),getItem(position).getTransportMean().getStationCirleDrawableId()));
        View separationView = item.findViewById(R.id.view_separation_transport_mean);
        separationView.setBackgroundColor(ContextCompat.getColor(getContext(),stationTripUi.getTransportMean().getColor()));
        //separationView.setVisibility(View.VISIBLE);
        View root = item.findViewById(R.id.root);

        if (isSelected(stationTripUi))
        {
            button.setActivated(true);

        }
        else
        {
            button.setActivated(false);


        }
        if (position==getCount()-1)
        {
            if (!addSeparationViewToLastItem)
            {
                separationView.setVisibility(View.GONE);
            }
        }
        else
        {
            separationView.setVisibility(View.VISIBLE);
        }

        return item;
    }

    private boolean isSelected (Station station)
    {
        if (selectedStation!=null)
        return station.getId() == this.selectedStation.getId();
        else
            return false;
    }
    public void selectStation (Station station)
    {
        selectedStation = station;
        notifyDataSetChanged();
    }


}