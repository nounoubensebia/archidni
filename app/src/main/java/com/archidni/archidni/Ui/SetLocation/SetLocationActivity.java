package com.archidni.archidni.Ui.SetLocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetLocationActivity extends AppCompatActivity {

    MapFragment mapView;
    @BindView(R.id.text_confirm)
    TextView confirmText;
    @BindView(R.id.text_my_position)
    TextView myPositionText;

    ArchidniGoogleMap archidniMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this,mapView, new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (archidniMap.getUserLocation()!=null)
                {
                    archidniMap.moveCamera(archidniMap.getUserLocation(),15);
                }
                else
                {
                    archidniMap.moveCamera(Coordinate.DEFAULT_LOCATION,15);
                }
            }
        });
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(IntentUtils.SET_LOCATION_COORDINATES,archidniMap.getCenter().toJson());
                setResult(IntentUtils.RESULT_OK,data);
                finish();
            }
        });
        myPositionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (archidniMap.getUserLocation()!=null)
                {
                    archidniMap.moveCamera(archidniMap.getUserLocation(),15);
                }
                else
                {
                    Toast.makeText(SetLocationActivity.this,
                            R.string.error_happened,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }
}
