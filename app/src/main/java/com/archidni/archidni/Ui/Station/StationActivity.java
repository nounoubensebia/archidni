package com.archidni.archidni.Ui.Station;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.Trip;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationActivity extends AppCompatActivity implements StationContract.View {

    @BindView(R.id.mapView)
    MapView mapView;
    /*@BindView(R.id.progressBar)
    ProgressBar progressBar;*/
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_name)
    TextView mainText;
    @BindView(R.id.text_coordinate)
    TextView coordinateText;
    @BindView(R.id.layout_path)
    View pathLayout;
    @BindView(R.id.layout_lines)
    View linesLayout;
    @BindView(R.id.layout_times)
    View timesLayout;
    @BindView(R.id.text_times)
    TextView timesText;
    @BindView(R.id.text_lines)
    TextView linesText;
    ArchidniMap archidniMap;
    StationContract.Presenter presenter;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Station station = Station.fromJson(getIntent().getExtras()
                .getString(IntentUtils.STATION_STATION));
        presenter = new StationPresenter(station,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        initViews(savedInstanceState);
        presenter.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                    presenter.onUserLocationCaptured(new Coordinate(location.getLatitude(),
                            location.getLongitude()));
            }
        });
    }

    private void initViews (Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        archidniMap = new ArchidniMap(mapView, savedInstanceState, new ArchidniMap.OnMapReadyCallback() {
            @Override
            public void onMapReady() {
                presenter.onMapReady();
            }
        });
        pathLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGetPathClicked();
            }
        });
        linesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleLinesTrips(true);
            }
        });
        timesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleLinesTrips(false);
            }
        });
    }

    @Override
    public void setTheme(Station station) {
        setTheme(station.getTransportMean().getTheme());
    }

    @Override
    public void showLinesLoadingBar() {
        recyclerView.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLinesOnList(ArrayList<? extends Line> lines) {

    }

    @Override
    public void showTripsOnList(ArrayList<? extends Trip> trips) {

    }

    @Override
    public void showTimeDialog(long selectedTime) {

    }

    @Override
    public void showDateDialog(long selectedDate) {

    }

    @Override
    public void updateTime(long newTime) {

    }

    @Override
    public void updateDate(long newDate) {

    }

    @Override
    public void startPathSearchActivity(Place origin, Place destination) {
        /*TODO check if null*/
        Intent intent = new Intent(this, PathSearchActivity.class);
        intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,origin.toJson());
        intent.putExtra(IntentUtils.PATH_SEARCH_DESTINATION,destination.toJson());
        startActivity(intent);
    }

    public void hideTitleText(final Station station) {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setTitle(" ");
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(station.getName());
                    pathLayout.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                    pathLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void showStationOnActivity(Station station) {
        mainText.setText(station.getName());
        coordinateText.setText(StringUtils.getLocationString(station.getCoordinate()));
        ViewUtils.changeTextViewState(this,coordinateText,station.getTransportMean()
                .getCoordinateDrawable(),ViewUtils.DIRECTION_LEFT);
        pathLayout.setBackgroundColor(ContextCompat.getColor(this,
                station.getTransportMean().getColor()));
        hideTitleText(station);
    }

    @Override
    public void showStationOnMap(Station station) {
        archidniMap.addMarker(station.getCoordinate(),station.getTransportMean().getMarkerIcon());
        archidniMap.moveCamera(station.getCoordinate(),15);
    }

    @Override
    public void updateLinesTripsLayout(boolean linesSelected,Station station) {
        if (linesSelected)
        {
            ViewUtils.changeTextViewState(this,linesText,
                    station.getTransportMean().getLinesSelectedDrawable()
                    ,station.getTransportMean().getColor(),ViewUtils.DIRECTION_RIGHT);
            ViewUtils.changeTextViewState(this,timesText,
                    R.drawable.ic_time_disabled,R.color.black
                    ,ViewUtils.DIRECTION_RIGHT);
        }
        else
        {
            ViewUtils.changeTextViewState(this,linesText,
                    R.drawable.ic_line_disabled
                    ,R.color.black,ViewUtils.DIRECTION_RIGHT);
            ViewUtils.changeTextViewState(this,timesText,station.getTransportMean()
                            .getTimesSelectedDrawable(),station.getTransportMean().getColor(),
                    ViewUtils.DIRECTION_RIGHT);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
