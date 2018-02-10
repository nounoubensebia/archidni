package com.archidni.archidni.Ui.Adapters;

/**
 * Created by nouno on 22/08/2017.
 */






import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by nouno on 07/07/2017.
 */

public class StationInsideLineAdapter extends ArrayAdapter<Station> {
    private Station selectedStation=null;


    public StationInsideLineAdapter(Context context, ArrayList<Station> stations, Station selectedStation)
    {
        super(context,0,stations);
        this.selectedStation = selectedStation;
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
        TextView StationLabelText = (TextView) item.findViewById(R.id.text_label);

        StationLabelText.setText(stationTripUi.getName());


        ImageView button = (ImageView)item.findViewById(R.id.layout_instruction1);
        button.setImageDrawable(ContextCompat.getDrawable(getContext(),getItem(position).getTransportMean().getStationCirleDrawableId()));
        View separationView = item.findViewById(stationTripUi.getTransportMean().getSeparationViewId());
        separationView.setVisibility(View.VISIBLE);
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
            Resources r = item.getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
            separationView.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)root.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,0,layoutParams.rightMargin,(int)px);
            root.setLayoutParams(layoutParams);

        }
        else
        {
            separationView.setVisibility(View.VISIBLE);
            if (position == 0)
            {
                Resources r = item.getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)root.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin,(int)px,layoutParams.rightMargin,0);
                root.setLayoutParams(layoutParams);

            }
            else
            {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)root.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                root.setLayoutParams(layoutParams);

            }}

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