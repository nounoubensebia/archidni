package com.archidni.archidni.Ui.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.StationInsideRideInstructionAdapter;

import java.util.ArrayList;

public class StopsDialog extends Dialog {

    private ArrayList<Station> stations;
    private StationInsideRideInstructionAdapter.OnStationClickListener onStationClickListener;

    public StopsDialog(@NonNull Context context, ArrayList<Station> stations, StationInsideRideInstructionAdapter.OnStationClickListener onStationClickListener) {
        super(context);
        this.stations = stations;
        this.onStationClickListener = onStationClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stops);
        RecyclerView recyclerView = findViewById(R.id.list_stops);
        StationInsideRideInstructionAdapter stationInsideRideInstructionAdapter =
                new StationInsideRideInstructionAdapter(getContext(),false, stations,new StationInsideRideInstructionAdapter.OnStationClickListener() {
                    @Override
                    public void onStationClick(Station station) {
                        onStationClickListener.onStationClick(station);
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(stationInsideRideInstructionAdapter);
    }
}
