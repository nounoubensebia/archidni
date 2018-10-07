package com.archidni.archidni.Ui.RealtimeBus;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.archidni.archidni.Data.RealtimeBus.BusRepository;
import com.archidni.archidni.Data.RealtimeBus.BusRepositoryImpl;
import com.archidni.archidni.LocationListener;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.RealtimeBus.Bus;
import com.archidni.archidni.Model.RealtimeBus.RealTimeBusFilter;
import com.archidni.archidni.R;
import com.archidni.archidni.TimeUtils;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealTimeBusActivity extends AppCompatActivity {


    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.text_update)
    View updateText;

    ArchidniGoogleMap archidniMap;

    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_bus);
        locationListener = new LocationListener(this);
        initViews();
        getBuses();
    }

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
    }

    private void showBusesOnMap (List<Bus> buses)
    {
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
            archidniMap.prepareMarker(bus.getCoordinate(),R.drawable.marker_bus,0,0
            ,String.format("Position prise il y'a %d minutes",
                            (TimeUtils.getCurrentTimeInSeconds()-bus.getTimeStamp()/1000)/60));
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
                RealTimeBusFilter realTimeBusFilter = new RealTimeBusFilter(buses,3600);
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

}
