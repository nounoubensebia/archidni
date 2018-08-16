package com.archidni.archidni.Ui.PathNavigation;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.LocationListener;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.MoveInstruction;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineInsideWaitInstructionAdapter;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PatternItem;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PathNavigationActivity extends AppCompatActivity implements PathNavigationContract.View {

    PathNavigationContract.Presenter presenter;

    ArchidniGoogleMap map;

    private boolean inflated = false;

    @BindView(R.id.text_my_position)
    TextView myPositionText;
    @BindView(R.id.layout_instruction)
    FrameLayout instructionLayout;

    private LocationListener locationListener;

    View inflatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        Path path = Path.fromJson(bundle.getString(IntentUtils.PATH));
        int selectedInstructionIndex = bundle.getInt(IntentUtils.INSTRUCTION_INDEX);
        presenter = new PathNavigationPresenter(this,path,selectedInstructionIndex);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_navigation);
        initViews(savedInstanceState);
        presenter.onCreate();
        locationListener = new LocationListener(this);
    }

    private void initViews (Bundle sis)
    {
        ButterKnife.bind(this);
        final MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        map = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map.setMyLocationEnabled(true);
            }
        }, new ArchidniGoogleMap.OnMapLoaded() {
            @Override
            public void onMapLoaded(Coordinate coordinate, LatLngBounds latLngBounds, double zoom) {
                presenter.onMapReady();
            }
        });
        myPositionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationListener.getLastKnownUserLocation(new LocationListener.OnUserLocationUpdated() {
                    @Override
                    public void onUserLocationUpdated(Coordinate userLocation) {
                        if (map.getMap().getCameraPosition().zoom<15)
                            map.animateCamera(userLocation,15,250);
                        else
                            map.animateCamera(userLocation,250);
                    }
                });

            }
        });
    }


    @Override
    public void showInstructionOnActivity(PathInstruction pathInstruction, int stepCount) {

        if (pathInstruction instanceof WalkInstruction)
        {
            instructionLayout.removeAllViews();
            instructionLayout.addView(LayoutInflater.from(this)
                    .inflate(R.layout.layout_path_step_walk,instructionLayout,false));
            showWalkInstructionOnActivity((WalkInstruction) pathInstruction);
        }

        if (pathInstruction instanceof RideInstruction)
        {
            instructionLayout.removeAllViews();
            instructionLayout.addView(LayoutInflater.from(this)
                    .inflate(R.layout.layout_path_step_ride,instructionLayout,false));
            showRideInstructionOnActivity((RideInstruction) pathInstruction);
        }

        if (pathInstruction instanceof WaitInstruction)
        {
            instructionLayout.removeAllViews();
            instructionLayout.addView(LayoutInflater.from(this)
                    .inflate(R.layout.layout_path_step_wait,instructionLayout,false));
            showWaitInstructionOnActivity((WaitInstruction) pathInstruction);
        }

        View nextImage = findViewById(R.id.image_next);
        View previousImage = findViewById(R.id.image_previous);
        TextView stepCountText = findViewById(R.id.text_step_count);


        stepCountText.setText("Etape "+stepCount);

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToNextInstruction();
            }
        });

        previousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToPreviousInstruction();
            }
        });
    }

    @Override
    public void showInstructionOnMap(PathInstruction pathStep, Path path) {
        if (pathStep instanceof MoveInstruction)
        {
            MoveInstruction moveInstruction = (MoveInstruction) pathStep;
            animateMapCameraToFitBounds(moveInstruction.getPolyline());
        }
        else
        {
            WaitInstruction waitInstruction = (WaitInstruction) pathStep;
            map.animateCamera(waitInstruction.getCoordinate(),15,500);
        }
    }

    private void showWalkInstructionOnActivity (WalkInstruction walkInstruction)
    {
        TextView durationText = findViewById(R.id.text_duration);
        TextView distanceText = findViewById(R.id.text_distance);
        TextView descriptionText = findViewById(R.id.text_description);

        descriptionText.setText(walkInstruction.getMainText());
        distanceText.setText(walkInstruction.getDistanceString());
        durationText.setText(walkInstruction.getDuration()+" minutes");
    }

    private void showRideInstructionOnActivity (RideInstruction rideInstruction)
    {
        ImageView transportMeanImage = findViewById(R.id.image_transport_mean_icon);
        TextView takeText = findViewById(R.id.text_take);
        TextView stopsText = findViewById(R.id.text_stops);

        transportMeanImage.setImageDrawable(ContextCompat.getDrawable(this,
                rideInstruction.getTransportMean().getIconEnabled()));
        takeText.setText("Prendre le "+rideInstruction.getTransportMean().getName());
        stopsText.setText(rideInstruction.getSections().size()+" arrets jusqu'à "
                +rideInstruction.getStations().get(rideInstruction.getStations().size()-1).getName());
        stopsText.setTextColor(ContextCompat.getColor(this,rideInstruction.getTransportMean().getColor()));
    }


    private void showWaitInstructionOnActivity (WaitInstruction waitInstruction)
    {
        ImageView timeImage = findViewById(R.id.image_time);
        TextView takeText = findViewById(R.id.text_take);
        TextView takeLinesText = findViewById(R.id.text_take_lines);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView moreLinesText = findViewById(R.id.text_more_lines);

        timeImage.setImageDrawable(ContextCompat.getDrawable(this,
                waitInstruction.getWaitLines().get(0).getLine().getTransportMean().getTimesSelectedDrawable()));
        takeText.setText("Attendre le "+waitInstruction.getWaitLines().get(0).getLine().getTransportMean().getName());
        takeLinesText.setText(waitInstruction.getTakeLineText());
        ArrayList<WaitLine> waitLinesToShow = new ArrayList<>();
        if (waitInstruction.getWaitLines().size()>2)
        {
            moreLinesText.setVisibility(View.VISIBLE);
            waitLinesToShow.add(waitInstruction.getWaitLines().get(0));
            waitLinesToShow.add(waitInstruction.getWaitLines().get(1));
        }
        else
        {
            moreLinesText.setVisibility(View.GONE);
            waitLinesToShow.addAll(waitInstruction.getWaitLines());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LineInsideWaitInstructionAdapter lineInsideWaitInstructionAdapter = new LineInsideWaitInstructionAdapter(
                this,
                waitLinesToShow,
                new LineInsideWaitInstructionAdapter.OnItemClick() {
                    @Override
                    public void onItemClick(WaitLine waitLine) {

                    }
                }

        );
        recyclerView.setAdapter(lineInsideWaitInstructionAdapter);
    }

    @Override
    public void showPathOnMap(Path path) {
        showInstructionsAnnotations(path);
        animateMapCameraToFitBounds(path.getPolyline());
    }


    private void animateMapCameraToFitBounds (ArrayList<Coordinate> polyline)
    {
        /*float mapWidth = ViewUtils.dpToPx(this,ViewUtils.getScreenWidthInDp(this));
        float mapHeight = ViewUtils.dpToPx(this,ViewUtils.getScreenHeightInDp(this))-
                instructionLayout.getHeight();
        map.animateCameraToBounds(polyline,(int)ViewUtils.dpToPx(this,32),(int) mapWidth,
                (int)mapHeight,250);*/
        map.animateCameraToBounds(polyline,(int)ViewUtils.dpToPx(this,32),500);
    }

    private void showInstructionsAnnotations(Path path)
    {
        ArrayList<Coordinate> pathPolyline = path.getPolyline();
        for (PathInstruction pathInstruction:path.getPathInstructions())
        {
            if (pathInstruction instanceof WalkInstruction)
            {
                ArrayList<PatternItem> patternItems = new ArrayList<>();
                patternItems.add(new Dot());
                patternItems.add(new Gap(5));
                map.preparePolyline(this,
                        ((WalkInstruction) pathInstruction).getPolyline(),
                        R.color.colorGreen,15,patternItems);
            }
            if (pathInstruction instanceof RideInstruction)
            {
                RideInstruction rideInstruction = (RideInstruction) pathInstruction;
                TransportMean transportMean = rideInstruction.getTransportMean();
                map.preparePolyline(this,
                        rideInstruction.getPolyline(),
                        transportMean.getColor(),15);
                for (Station station: rideInstruction.getStations())
                {
                    map.prepareMarker(station.getCoordinate(),
                            transportMean.getMarkerInsideLineDrawable(),0.5f,0.5f,station.getName());
                }
            }
        }
        map.addMarker(pathPolyline.get(0),R.drawable.ic_marker_blue_24dp);
        map.addMarker(pathPolyline.get(pathPolyline.size()-1),R.drawable.ic_marker_red_24dp);
        map.addPreparedAnnotations();
    }




}
