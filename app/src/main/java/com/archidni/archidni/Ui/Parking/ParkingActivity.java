package com.archidni.archidni.Ui.Parking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

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
        presenter.onCreate();
    }



    @Override
    public void showParkingOnActivity(Parking parking) {
        coordinateText.setText(parking.getCoordinate().getLatitude()+","+parking.getCoordinate().getLongitude());
        capacityText.setText("Capacité : "+parking.getCapacity()+" places");
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
}
