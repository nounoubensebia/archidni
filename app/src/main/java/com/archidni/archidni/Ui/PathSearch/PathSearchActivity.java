package com.archidni.archidni.Ui.PathSearch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;


import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Search.SearchActivity;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PathSearchActivity extends AppCompatActivity implements PathSearchContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.layout_path_suggestions)
    View pathSuggestionsLayout;
    @BindView(R.id.text_get_path)
    TextView getPathText;
    @BindView(R.id.list_paths)
    RecyclerView pathsList;
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

    ArchidniMap archidniMap;
    PathSearchPresenter pathSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_search);
        initViews(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        pathSearchPresenter = new PathSearchPresenter(this,
                Place.fromJson(extras.getString(IntentUtils.PATH_SEARCH_ORIGIN)),
                Place.fromJson(extras.getString(IntentUtils.PATH_SEARCH_DESTINATION)));
    }

    private void initViews(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        archidniMap = new ArchidniMap(mapView, savedInstanceState,
                new ArchidniMap.OnMapReadyCallback() {
            @Override
            public void onMapReady() {
                pathSearchPresenter.onMapReady();
            }
        });
        getPathLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.loadPathSuggestions();
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

    @Override
    public void showOriginAndDestinationLabels(String origin, String destination) {
        String originString = (!origin.equals("")) ? origin:getString(R.string.not_defined);
        String destinationString = (!destination.equals("")) ? destination:getString(R.string.not_defined);
        originText.setText(originString);
        destinationText.setText(destinationString);
    }

    @Override
    public void showOriginAndDestinationOnMap(Place origin, Place destination) {
        archidniMap.clearMap();
        archidniMap.addMarker(destination.getCoordinate(),R.drawable.ic_marker_red_24dp);
        if (origin!=null)
        {
            archidniMap.addMarker(origin.getCoordinate(),R.drawable.ic_marker_blue_24dp);
            ArrayList<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(origin.getCoordinate());
            coordinates.add(destination.getCoordinate());
            archidniMap.moveCameraToBounds(coordinates, (int) ViewUtils.dpToPx(this,64),
                    (int) ViewUtils.dpToPx(this,64),
                    (int) ViewUtils.dpToPx(this,64),
                    (int) ViewUtils.dpToPx(this,128));
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
        getPathText.setVisibility(View.GONE);
        pathsListLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPathSuggestions(ArrayList<Path> paths) {
        progressBar.setVisibility(View.GONE);
        pathSuggestionsLayout.setVisibility(View.VISIBLE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IntentUtils.RESULT_OK)
            pathSearchPresenter.onActivityResult(requestCode,
                    Place.fromJson(data.getExtras().getString(IntentUtils.LOCATION)));
    }
}
