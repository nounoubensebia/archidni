package com.archidni.archidni.Ui.RealtimeBus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.archidni.archidni.Data.RealtimeBus.BusRepository;
import com.archidni.archidni.Data.RealtimeBus.BusRepositoryImpl;
import com.archidni.archidni.Model.RealtimeBus.Bus;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealTimeBusActivity extends AppCompatActivity {


    @BindView(R.id.progressBar)
    View progressBar;

    ArchidniGoogleMap archidniMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_bus);
        initViews();
        getBuses();
    }

    private void initViews()
    {
        ButterKnife.bind(this);
        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });
    }

    private void showBusesOnMap (List<Bus> buses)
    {
        for (Bus bus:buses)
        {
            archidniMap.prepareMarker(bus.getCoordinate(),R.drawable.ic_marker_blue_24dp,0,0);
        }
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
        showProgressBar();
        BusRepository busRepository = new BusRepositoryImpl();
        busRepository.getBuses(new BusRepository.OnBusesFound() {
            @Override
            public void onBusesFound(List<Bus> buses) {
                showBusesOnMap(buses);
                hideProgressBar();
            }

            @Override
            public void onError() {
                showErrorMessage();
                hideProgressBar();
            }
        });
    }

    private void showErrorMessage()
    {
        Toast.makeText(this,R.string.error_happened,Toast.LENGTH_SHORT).show();
    }

}
