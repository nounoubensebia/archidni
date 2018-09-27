package com.archidni.archidni.Ui.PathDetails;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
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
import com.archidni.archidni.Ui.Report.ReportAlertChooseLineActivity;
import com.archidni.archidni.Ui.Report.ReportInformationExplainProblemActivity;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.DialogUtils;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PatternItem;
import com.google.gson.Gson;

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
    @BindView(R.id.text_path_is_correct)
    TextView pathIsCorrectText;
    @BindView(R.id.text_path_is_incorrect)
    TextView pathIsIncorrectText;
    @BindView(R.id.text_report)
    TextView reportText;

    private Dialog waitDialog;

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
                archidniMap.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
                archidniMap.getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (marker.getTag()!=null&&marker.getTag() instanceof  Station)
                            presenter.onStationItemClick(PathDetailsActivity.this,(Station)marker.getTag());
                    }
                });
            }
        });
        
        startNavigationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartNavigationClick();
            }
        });
        pathIsCorrectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPathIsCorrectClick();
            }
        });
        pathIsIncorrectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPathIsIncorrectClick();
            }
        });
        reportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onReportClick();
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
                    public void onItemSelected(PathInstruction pathInstruction, int position) {
                        startPathNavigationActivity(path,position);
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
                            transportMean.getMarkerInsideLineDrawable(),0.5f,0.5f,station.getName()
                    ,station);
                }
            }
        }
        archidniMap.addMarker(pathPolyline.get(0),R.drawable.marker_departure);
        archidniMap.addMarker(pathPolyline.get(pathPolyline.size()-1),R.drawable.marker_arrival);
        archidniMap.addPreparedAnnotations();
    }

    @Override
    public void showPathOnMap(Path path) {
        ArrayList<Coordinate> pathPolyline = path.getPolyline();
        showInstructionsAnnotations(path);
        archidniMap.animateCameraToBounds(pathPolyline,(int)ViewUtils.dpToPx(this,32),500);
    }

    @Override
    public void startPathNavigationActivity(Path path,int instructionIndex) {
        Intent intent = new Intent(this, PathNavigationActivity.class);
        intent.putExtra(IntentUtils.PATH,path.toJson());
        intent.putExtra(IntentUtils.INSTRUCTION_INDEX,instructionIndex);
        startActivity(intent);
    }

    @Override
    public void showWaitDialog() {
        waitDialog = DialogUtils.buildProgressDialog("Veuillez patientez",this);
        waitDialog.show();
    }

    @Override
    public void hideWaitDialog() {
        waitDialog.hide();
    }

    @Override
    public void startLineActivity(Line line) {
        Intent intent = new Intent(this, LineActivity.class);
        intent.putExtra(IntentUtils.LINE_LINE,line.toJson());
        startActivity(intent);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,"Une erreur s'est produite",Toast.LENGTH_LONG).show();
    }

    @Override
    public void startStationActivity(Station station) {
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
        startActivity(intent);
    }

    @Override
    public void startPathReportActivity(Path path) {
        Intent intent = new Intent(this, ReportInformationExplainProblemActivity.class);
        intent.putExtra(IntentUtils.PATH,new Gson().toJson(path));
        intent.putExtra(IntentUtils.IS_PATH_GOOD,false);
        startActivity(intent);
    }

    @Override
    public void startDisruptionReportActivity() {
        Intent intent = new Intent(this, ReportAlertChooseLineActivity.class);
        startActivity(intent);
    }

    @Override
    public void showReportSentMessage() {
        Toast.makeText(this,R.string.report_sent,Toast.LENGTH_LONG).show();
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
