package com.archidni.archidni.Ui.PathDetails;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineInsideWaitInstructionAdapter;
import com.archidni.archidni.Ui.Adapters.PathInstructionAdapter;
import com.archidni.archidni.Ui.Adapters.PathInstructionRecyclerAdapter;
import com.archidni.archidni.Ui.Adapters.StationInsideRideInstructionAdapter;
import com.archidni.archidni.Ui.Line.LineActivity;
import com.archidni.archidni.Ui.PathNavigation.PathNavigationActivity;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.UiUtils.DialogUtils;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PathDetailsActivity extends AppCompatActivity implements PathDetailsContract.View {

    @BindView(R.id.list_instructions)
    RecyclerView instructionsList;
    MapFragment mapView;
    @BindView(R.id.text_start_navigation)
    TextView startNavigationText;
    @BindView(R.id.text_total_duration)
    TextView durationText;
    @BindView(R.id.scrollView)
    View scrollView;

    private Dialog lineSearchDialog;

    private ArchidniGoogleMap archidniMap;

    private PathDetailsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Path path = Path.fromJson(getIntent().getExtras().getString(IntentUtils.PATH));
        presenter = new PathDetailsPresenter(path,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_details);
        initViews(savedInstanceState);
        presenter.onCreate();
    }

    private void initViews (Bundle bundle)
    {
        ButterKnife.bind(this);

        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this,mapView, new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                presenter.onMapReady();
            }
        });
        
        startNavigationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartNavigationClick();
            }
        });
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void showPathOnActivity(final Path path) {
        durationText.setText(path.getDuration()/60+" minutes");
        ArrayList<PathInstruction> pathInstructions = path.getPathInstructions();
        final PathInstructionAdapter pathInstructionAdapter = new PathInstructionAdapter(this,pathInstructions);
        /*pathInstructionAdapter.setOnCoordinateSelected(new PathInstructionAdapter.OnCoordinateSelected() {
            @Override
            public void onCoordinateSelected(Coordinate coordinate, int selectedItem) {
                if (selectedItem!=-1)
                {
                    archidniMap.clearMap();
                    archidniMap.animateCamera(coordinate,16,500);
                    showInstructionsAnnotations(path);
                }
                else
                {
                    archidniMap.clearMap();
                    archidniMap.animateCameraToBounds(path.getPolyline(),100,500);
                    showInstructionsAnnotations(path);
                }
                pathInstructionAdapter.selectElement(selectedItem);
            }
        });
        pathInstructionAdapter.setOnPolylineSelected(new PathInstructionAdapter.OnPolylineSelected() {
            @Override
            public void onPolylineSelected(ArrayList<Coordinate> polyline, int selectedItem) {
                if (selectedItem!=-1)
                {
                    archidniMap.clearMap();
                    archidniMap.animateCameraToBounds(polyline,50,500);
                    archidniMap.preparePolyline(PathDetailsActivity.this,polyline,R.color.black,15);
                    archidniMap.addPreparedAnnotations();
                    showInstructionsAnnotations(path);

                }
                else
                {
                    archidniMap.clearMap();
                    archidniMap.animateCameraToBounds(path.getPolyline(),100,500);
                    showInstructionsAnnotations(path);
                }
                pathInstructionAdapter.selectElement(selectedItem);
            }
        });*/
        //instructionsList.setAdapter(pathInstructionAdapter);
        //instructionsList.setDividerHeight(0);
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewUtils.justifyListViewHeightBasedOnChildren(instructionsList);
            }
        },250);*/

        instructionsList.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        instructionsList.setItemAnimator(new DefaultItemAnimator());
        PathInstructionRecyclerAdapter pathInstructionRecyclerAdapter =
                new PathInstructionRecyclerAdapter(this, path.getPathInstructions(), new LineInsideWaitInstructionAdapter.OnItemClick() {
                    @Override
                    public void onItemClick(WaitLine waitLine) {
                        presenter.onLineItemClick(PathDetailsActivity.this, waitLine.getLine());
                    }
                }, new StationInsideRideInstructionAdapter.OnStationClickListener() {
                    @Override
                    public void onStationClick(Station station) {
                        presenter.onStationItemClick(PathDetailsActivity.this, station);
                    }
                }, new PathInstructionRecyclerAdapter.OnItemSelected() {
                    @Override
                    public void onRideInstructionSelected(RideInstruction rideInstruction) {
                        //archidniMap.animateCameraToBounds(rideInstruction.getPolyline(),
                        //        (int)ViewUtils.dpToPx(PathDetailsActivity.this,30),250);
                    }

                    @Override
                    public void onWalkInstructionSelected(WalkInstruction walkInstruction) {
                        //archidniMap.animateCameraToBounds(walkInstruction.getPolyline(),
                        //        (int)ViewUtils.dpToPx(PathDetailsActivity.this,30),250);
                    }

                    @Override
                    public void onWaitInstructionSelected(WaitInstruction waitInstruction) {
                        //archidniMap.animateCamera(waitInstruction.getCoordinate(),15,250);
                    }
                });
        instructionsList.setAdapter(pathInstructionRecyclerAdapter);


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
                archidniMap.preparePolyline(this,
                        ((WalkInstruction) pathInstruction).getPolyline(),
                        R.color.colorGreen,15,patternItems);
            }
            if (pathInstruction instanceof RideInstruction)
            {
                RideInstruction rideInstruction = (RideInstruction) pathInstruction;
                TransportMean transportMean = rideInstruction.getTransportMean();
                archidniMap.preparePolyline(this,
                        rideInstruction.getPolyline(),
                        transportMean.getColor(),15);
                for (Station station: rideInstruction.getStations())
                {
                    archidniMap.prepareMarker(station.getCoordinate(),
                            transportMean.getMarkerInsideLineDrawable(),0.5f,0.5f,station.getName());
                }
            }
        }
        archidniMap.addMarker(pathPolyline.get(0),R.drawable.ic_marker_blue_24dp);
        archidniMap.addMarker(pathPolyline.get(pathPolyline.size()-1),R.drawable.ic_marker_red_24dp);
        archidniMap.addPreparedAnnotations();
    }

    @Override
    public void showPathOnMap(Path path) {
        ArrayList<Coordinate> pathPolyline = path.getPolyline();
        showInstructionsAnnotations(path);
        archidniMap.animateCameraToBounds(pathPolyline,100,500);
    }

    @Override
    public void startPathNavigationActivity(Path path) {
        Intent intent = new Intent(this, PathNavigationActivity.class);
        intent.putExtra(IntentUtils.PATH,path);
        startActivity(intent);
    }

    @Override
    public void showLineSearchDialog() {
        lineSearchDialog = DialogUtils.buildProgressDialog("Veuillez patientez",this);
        lineSearchDialog.show();
    }

    @Override
    public void hideLineSearchDialog() {
        lineSearchDialog.hide();
    }

    @Override
    public void startLineActivity(Line line) {
        Intent intent = new Intent(this, LineActivity.class);
        intent.putExtra(IntentUtils.LINE_LINE,line.toJson());
        startActivity(intent);
    }

    @Override
    public void showLineSearchError() {
        Toast.makeText(this,"Une erreur s'est produite",Toast.LENGTH_LONG).show();
    }

    @Override
    public void startStationActivity(Station station) {
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
        startActivity(intent);
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
