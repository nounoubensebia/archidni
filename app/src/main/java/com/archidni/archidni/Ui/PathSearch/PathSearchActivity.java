package com.archidni.archidni.Ui.PathSearch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.PathSuggestionAdapter;
import com.archidni.archidni.Ui.PathDetails.PathDetailsActivity;
import com.archidni.archidni.Ui.Search.SearchActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PathSearchActivity extends AppCompatActivity implements PathSearchContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MapFragment mapView;
    @BindView(R.id.layout_path_suggestions)
    View pathSuggestionsLayout;
    @BindView(R.id.text_get_path)
    TextView getPathText;
    @BindView(R.id.list_paths)
    RecyclerView recyclerView;
    @BindView(R.id.layout_paths_list)
    View pathsListLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout_bottom)
    View bottomLayout;
    @BindView(R.id.text_origin)
    TextView originText;
    @BindView(R.id.text_destination)
    TextView destinationText;
    @BindView(R.id.layout_get_path)
    View getPathLayout;
    @BindView(R.id.layout_origin)
    View originLayout;
    @BindView(R.id.layout_destination)
    View destinationLayout;
    @BindView(R.id.text_departure_time)
    TextView departureTimeText;
    @BindView(R.id.text_departure_date)
    TextView departureDateText;
    @BindView(R.id.layout_path_and_map)
    View pathAndMapLayout;
    @BindView(R.id.layout_map_loading)
    View mapLoadingLayout;

    ArchidniGoogleMap archidniMap;
    PathSearchContract.Presenter pathSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_search);
        initViews(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        pathSearchPresenter = new PathSearchPresenter(this,
                PathPlace.fromJson(extras.getString(IntentUtils.PATH_SEARCH_ORIGIN)),
                PathPlace.fromJson(extras.getString(IntentUtils.PATH_SEARCH_DESTINATION)));
    }



    private void initViews(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);

        archidniMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                pathSearchPresenter.onMapReady();
            }
        }, new ArchidniGoogleMap.OnMapLoaded() {
            @Override
            public void onMapLoaded(Coordinate coordinate, LatLngBounds latLngBounds, double zoom) {
                pathSearchPresenter.onMapLoaded();
            }
        });

        getPathLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.onSearchPathsClick(PathSearchActivity.this);
            }
        });
        destinationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.lookForLocation(IntentUtils.SearchIntents.TYPE_LOOK_FOR_DEST);
            }
        });
        originLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.lookForLocation(IntentUtils.SearchIntents.TYPE_LOOK_FOR_OR);
            }
        });
        departureTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.onDepartureTimeClick();
            }
        });
        departureDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.onDepartureDateClick();
            }
        });
    }



    @Override
    public void showOriginAndDestinationLabels(String origin, String destination) {
        String originString = (!origin.equals("")) ? origin:getString(R.string.not_defined);
        String destinationString = (!destination.equals("")) ? destination:getString(R.string.not_defined);
        originText.setText(originString);
        destinationText.setText(destinationString);
    }

    @Override
    public void showOriginAndDestinationOnMap(PathPlace origin, PathPlace destination) {
        archidniMap.clearMap();
        archidniMap.addMarker(destination.getCoordinate(),R.drawable.ic_marker_red_24dp);
        if (origin!=null)
        {
            archidniMap.addMarker(origin.getCoordinate(),R.drawable.ic_marker_blue_24dp);
            ArrayList<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(origin.getCoordinate());
            coordinates.add(destination.getCoordinate());
            archidniMap.animateCameraToBounds(coordinates, (int) ViewUtils.dpToPx(this,128),
                    1000
                    );
        }
        else
        {
            archidniMap.moveCamera(destination.getCoordinate(),15);
        }
    }

    @Override
    public void showLoadingBar() {
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) bottomLayout.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        bottomLayout.setLayoutParams(layoutParams);
        progressBar.setVisibility(View.VISIBLE);
        getPathLayout.setVisibility(View.GONE);
        pathsListLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPathSuggestions(ArrayList<Path> paths,PathSettings pathSettings) {
        progressBar.setVisibility(View.GONE);
        pathSuggestionsLayout.setVisibility(View.VISIBLE);
        PathSuggestionAdapter pathSuggestionAdapter = new PathSuggestionAdapter(this,paths,
                pathSettings);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        pathSuggestionAdapter.setOnClickListener(new PathSuggestionAdapter.OnClickListener() {
            @Override
            public void onClick(Path path) {
                pathSearchPresenter.onPathItemClick(path);
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pathSuggestionAdapter);
    }

    @Override
    public void startSearchActivity(int requestType) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE,requestType);
        startActivityForResult(intent,requestType);
    }

    @Override
    public void showSetTimeDialog(long departureTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(departureTime*1000);
        TimePickerDialog dialogFragment = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pathSearchPresenter.updateTime(hourOfDay * 3600 + minute * 60);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialogFragment.show();
    }

    @Override
    public void showSetDateDialog(long departureDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(departureDate*1000);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0);
                pathSearchPresenter.updateDate(c.getTimeInMillis() / 1000);
            }
        }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void updateTime(long departureTime) {
        departureTimeText.setText(StringUtils.getTimeString(departureTime));
    }

    @Override
    public void updateDate(long departureDate) {
        departureDateText.setText(StringUtils.getDateString(departureDate));
    }

    @Override
    public void hidePathsLayout() {
        pathsListLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CoordinatorLayout.LayoutParams layoutParams =
                        (CoordinatorLayout.LayoutParams) bottomLayout.getLayoutParams();
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                getPathLayout.setVisibility(View.VISIBLE);
                bottomLayout.setLayoutParams(layoutParams);
                getPathText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                pathSuggestionsLayout.setVisibility(View.GONE);
            }
        },100);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,"Une erreur s'est produite veuillez réessayez"
                ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showOriginNotSet() {
        Toast.makeText(this,"Veuillez choisir un point de départ",Toast.LENGTH_LONG).show();
    }

    @Override
    public void moveCameraToCoordinate(Coordinate coordinate) {
        archidniMap.moveCamera(coordinate,15);
    }

    @Override
    public void startPathDetailsActivity(Path path) {
        Intent intent = new Intent(this, PathDetailsActivity.class);
        intent.putExtra(IntentUtils.PATH,path);
        startActivity(intent);
    }

    @Override
    public void hideMapLoadingLayout() {
        mapLoadingLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pathAndMapLayout.setVisibility(View.VISIBLE);
            }
        },250);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IntentUtils.RESULT_OK)
            pathSearchPresenter.onActivityResult(requestCode,
                    PathPlace.fromJson(data.getExtras().getString(IntentUtils.LOCATION)));
    }
}
