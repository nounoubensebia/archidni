package com.archidni.archidni.Ui.Parking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Places.Place;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.PlaceAdapter;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingActivity extends AppCompatActivity implements ParkingContract.View {


    @BindView(R.id.text_coordinate)
    TextView coordinateText;
    @BindView(R.id.text_capacity)
    TextView capacityText;
    @BindView(R.id.text_name)
    TextView nameText;
    @BindView(R.id.layout_path)
    View pathLayout;
    @BindView(R.id.layout_nearby)
    View nearbyLayout;
    @BindView(R.id.recyclerView)
    RecyclerView nearbyPlacesList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout_error)
    View errorLayout;
    @BindView(R.id.button_retry)
    Button retryButton;
    @BindView(R.id.layout_details)
    View detailsLayout;

    MapFragment mapView;

    ArchidniGoogleMap archidniMap;

    private ParkingContract.Presenter presenter;

    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        Parking parking = Parking.fromJson(bundle.getString(IntentUtils.PARKING));
        presenter = new ParkingPresenter(parking,this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                    presenter.onUserLocationCaptured(new Coordinate(location.getLatitude(),
                            location.getLongitude()));
            }
        });
        pathLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGetPathClicked();
            }
        });
        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this,mapView, new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                presenter.onMapReady();
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onRetryClick();
            }
        });
        presenter.onCreate();
    }



    @Override
    public void showParkingOnActivity(Parking parking) {
        coordinateText.setText(parking.getCoordinate().getLatitude()+","+parking.getCoordinate().getLongitude());
        if (parking.getCapacity()!=-1)
            capacityText.setText("Capacité : "+parking.getCapacity()+" places");
        else
            detailsLayout.setVisibility(View.GONE);
        nameText.setText(parking.getName());
    }

    @Override
    public void startPathSearchActivity(PathPlace origin, PathPlace destination) {
        Intent intent = new Intent(this, PathSearchActivity.class);
        if (origin!=null)
            intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,origin.toJson());
        else
            intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,new Gson().toJson(null));
        intent.putExtra(IntentUtils.PATH_SEARCH_DESTINATION,destination.toJson());
        startActivity(intent);
    }

    @Override
    public void showParkingOnMap(Parking parking) {
        archidniMap.moveCamera(parking.getCoordinate(),14);
        archidniMap.addMarker(parking.getCoordinate(),R.drawable.marker_parking);
    }

    @Override
    public void showNearbyPlacesOnList(ArrayList<MainActivityPlace> places, Parking parking) {
        PlaceAdapter placeAdapter = new PlaceAdapter(this, places, parking.getCoordinate()
                , new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainActivityPlace mainListPlace) {
                presenter.onPlaceClicked(mainListPlace);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        nearbyPlacesList.setLayoutManager(mLayoutManager);
        nearbyPlacesList.setItemAnimator(new DefaultItemAnimator());
        nearbyPlacesList.setAdapter(placeAdapter);
    }

    @Override
    public void startPlaceActivity(MainActivityPlace mainActivityPlace) {
        if (mainActivityPlace instanceof Station)
        {
            Station station = (Station) mainActivityPlace;
            Intent intent = new Intent(this, StationActivity.class);
            intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
            startActivity(intent);
        }
        else
        {
            if (mainActivityPlace instanceof Parking)
            {
                Parking parking = (Parking) mainActivityPlace;
                Intent intent = new Intent(this,ParkingActivity.class);
                intent.putExtra(IntentUtils.PARKING,parking.toJson());
                startActivity(intent);
            }
        }
    }

    @Override
    public void showNearbyPlacesLayout() {
        nearbyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNearbyPlacesLayout() {
        nearbyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLayout() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorLayout() {
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void showProgressLayout() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressLayout() {
        progressBar.setVisibility(View.GONE);
    }
}
