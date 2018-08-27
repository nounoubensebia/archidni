package com.archidni.archidni.Ui.Line.Schedule.TrainTrip;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationTime;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.StationInsideScheduleAdapter;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainTripActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        TrainSchedule trainSchedule = new Gson().fromJson(extras.getString(IntentUtils.SCHEDULE),TrainSchedule.class);
        setTheme(trainSchedule.getTransportMean().getTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_trip);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(4);
        }
        ButterKnife.bind(this);
        populateList(trainSchedule);
    }

    private void populateList (TrainSchedule trainSchedule)
    {
        StationInsideScheduleAdapter stationInsideScheduleAdapter = new StationInsideScheduleAdapter(
                this, trainSchedule, new StationInsideScheduleAdapter.OnItemClick() {
            @Override
            public void onItemClick(StationTime stationTime) {
                startStationActivity(stationTime.getStation());
            }
        }
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(stationInsideScheduleAdapter);
    }

    private void startStationActivity (Station station)
    {
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
        startActivity(intent);
    }
}
