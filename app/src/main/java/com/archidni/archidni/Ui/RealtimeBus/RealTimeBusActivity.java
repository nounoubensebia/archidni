package com.archidni.archidni.Ui.RealtimeBus;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.Data.RealtimeBus.BusRepository;
import com.archidni.archidni.Data.RealtimeBus.BusRepositoryImpl;
import com.archidni.archidni.LocationListener;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.RealtimeBus.Bus;
import com.archidni.archidni.Model.RealtimeBus.RealTimeBusFilter;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.TimeUtils;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealTimeBusActivity extends AppCompatActivity {


    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.text_update)
    TextView updateText;

    ArchidniGoogleMap archidniMap;

    LocationListener locationListener;

    private Runnable updateTimerTask;

    private int timerValue;

    private Timer timer;

    private boolean runTimer = false;

    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_bus);
        locationListener = new LocationListener(this);
        initViews();
        getBuses();
    }

    @SuppressLint("HandlerLeak")
    private void initViews()
    {
        ButterKnife.bind(this);
        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (locationListener.getUserLocation()!=null)
                {
                    archidniMap.animateCamera(locationListener.getUserLocation(),14,250);
                }
                else
                {
                    archidniMap.animateCamera(Coordinate.DEFAULT_LOCATION,14,250);
                }
                googleMap.setTrafficEnabled(true);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        archidniMap.animateCamera(new Coordinate(marker.getPosition().latitude,
                                marker.getPosition().longitude),250);
                        return true;
                    }
                });
            }
        });
        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBuses();
            }
        });
        timer = new Timer();
        timerHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    if (runTimer)
                    {
                        if (timerValue>0)
                        {
                            updateText.setText(""+timerValue);
                            updateText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(RealTimeBusActivity.this,"Veuillez attendre au moins 60 secondes avant de pouvoir re-actualiser les positions des bus"
                                    ,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            runTimer = false;
                            timerValue = 60;
                            updateText.setText("Actualiser");
                            updateText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getBuses();
                                }
                            });
                        }
                    }
                }
            };
        startTimeUpdater();
    }

    private void startTimeUpdater ()
    {

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (runTimer)
                {
                    timerValue-=1;
                    timerHandler.obtainMessage(1).sendToTarget();
                }
            }
        }, 0, 1000);
    }

    private void showBusesOnMap (List<Bus> buses)
    {
        timerValue = 60;
        runTimer = true;
        archidniMap.clearMap();
        for (Bus bus:buses)
        {
            Log.i("CURRENT_TIME",TimeUtils.getCurrentTimeInSeconds()+"");
            Log.i("BUS_TIME",bus.getTimeStamp()/1000+"");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(bus.getTimeStamp());
            Calendar calendar1 =  Calendar.getInstance();
            calendar1.setTimeInMillis(TimeUtils.getCurrentTimeInSeconds()*1000);
            Log.i("DIFF",String.format("%s %s %s %s %s",TimeUtils.getTimeString(calendar1),
                    TimeUtils.getDateString(calendar1),TimeUtils.getTimeString(calendar),TimeUtils.getDateString(calendar),
                    TimeUtils.getCurrentTimeInSeconds()-bus.getTimeStamp()/1000));
            int rot = bus.getCourse();
            archidniMap.prepareMarker(bus.getCoordinate(),R.drawable.shape_blue_circle,0.5f,0.5f
            ,String.format("Position prise à %s",
                            (TimeUtils.getTimeString(calendar))),rot-90);

            /*if (rot>180)
                rot-=360;*/
            if (bus.getSpeed()>10) {
                archidniMap.prepareMarker(bus.getCoordinate(), R.drawable.ic_arrow_forward_black_24dp, 0.5f, 0.5f
                        , String.format("Position prise à %s",
                                (TimeUtils.getTimeString(calendar))), rot-90);

            }
            else
                archidniMap.prepareMarker(bus.getCoordinate(), R.drawable.ic_format_color_text_black_24dp, 0.5f, 0.5f
                        , String.format("Position prise à %s",
                                (TimeUtils.getTimeString(calendar))), 0);
        }
    }

    private void showUpdateButton()
    {
        updateText.setVisibility(View.VISIBLE);
    }

    private void hideUpdateButton()
    {
        updateText.setVisibility(View.GONE);
    }

    private void showProgressBar ()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }

    private void getBuses ()
    {
        hideUpdateButton();
        showProgressBar();
        BusRepository busRepository = new BusRepositoryImpl();
        busRepository.getBuses(new BusRepository.OnBusesFound() {
            @Override
            public void onBusesFound(List<Bus> buses) {
                RealTimeBusFilter realTimeBusFilter = new RealTimeBusFilter(buses,7200);
                showBusesOnMap(realTimeBusFilter.getFilteredBuses());
                hideProgressBar();
                showUpdateButton();
            }

            @Override
            public void onError() {
                showErrorMessage();
                hideProgressBar();
                showUpdateButton();
            }
        });
    }

    private void showErrorMessage()
    {
        Toast.makeText(this,R.string.error_happened,Toast.LENGTH_SHORT).show();
    }

    static class UpdateTimerTask extends TimerTask
    {

        @Override
        public void run() {

        }
    }

}
