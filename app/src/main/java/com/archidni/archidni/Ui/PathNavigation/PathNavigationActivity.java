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
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineInsideWaitInstructionAdapter;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


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
    }

    private void initViews (Bundle sis)
    {
        ButterKnife.bind(this);
        MapFragment mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        map = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                presenter.onMapReady();
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
        takeText.setText("prendre le "+waitInstruction.getWaitLines().get(0).getLine().getTransportMean().getName());
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

    }




}
