package com.archidni.archidni.Ui.PathSearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathPreferences;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.TimeUtils;
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
    @BindView(R.id.text_options)
    TextView optionsText;
    @BindView(R.id.text_no_paths_found)
    TextView noPathsFoundText;
    @BindView(R.id.radio_group_departure_arrival)
    RadioGroup arrivalDeparturerRadioGroup;
    @BindView(R.id.layout_search_error)
    LinearLayout searchErrorLayout;
    @BindView(R.id.button_retry)
    Button retryButton;

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
                boolean arrival = false;
                if (arrivalDeparturerRadioGroup.getCheckedRadioButtonId()==R.id.radio_button_arrival)
                    arrival = true;
                    pathSearchPresenter.onSearchPathsClick(PathSearchActivity.this,arrival);
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
        optionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathSearchPresenter.onOptionsLayoutClicked();
            }
        });
        arrivalDeparturerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hidePathsLayout();
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean arrival = false;
                if (arrivalDeparturerRadioGroup.getCheckedRadioButtonId()==R.id.radio_button_arrival)
                    arrival = true;
                pathSearchPresenter.onSearchPathsClick(PathSearchActivity.this,arrival);
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
        archidniMap.addMarker(destination.getCoordinate(),R.drawable.marker_arrival);
        if (origin!=null)
        {
            archidniMap.addMarker(origin.getCoordinate(),R.drawable.marker_departure);
            ArrayList<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(origin.getCoordinate());
            coordinates.add(destination.getCoordinate());
            archidniMap.animateCameraToBounds(coordinates, (int) ViewUtils.dpToPx(this,32),
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
        searchErrorLayout.setVisibility(View.GONE);
        pathsListLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPathSuggestions(ArrayList<Path> paths,PathSettings pathSettings) {
        progressBar.setVisibility(View.GONE);
        //recyclerView.setNestedScrollingEnabled(false);
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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pathSuggestionAdapter);
        if (paths.size()==0)
        {
            noPathsFoundText.setVisibility(View.VISIBLE);
        }
        else
        {
            noPathsFoundText.setVisibility(View.GONE);
        }
    }

    @Override
    public void startSearchActivity(int requestType) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE,requestType);
        startActivityForResult(intent,requestType);
    }

    @Override
    public void showSetTimeDialog(final Calendar departureTime) {

        TimePickerDialog dialogFragment = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(departureTime.getTimeInMillis());
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                pathSearchPresenter.updateTime(calendar);
            }
        }, departureTime.get(Calendar.HOUR_OF_DAY), departureTime.get(Calendar.MINUTE), true);
        dialogFragment.show();
    }

    @Override
    public void showSetDateDialog(final Calendar departureTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(departureTime.getTimeInMillis());
                c.set(year, month, dayOfMonth);
                pathSearchPresenter.updateDate(c);
            }
        }, departureTime.get(Calendar.YEAR), departureTime.get(Calendar.MONTH), departureTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void updateTime(Calendar departureTime) {
        departureTimeText.setText(TimeUtils.getTimeString(departureTime));
    }

    @Override
    public void updateDate(Calendar departureTime) {
        departureDateText.setText(StringUtils.getDateString(departureTime.getTimeInMillis()/1000));
    }

    @Override
    public void hidePathsLayout() {

        if (searchErrorLayout.getVisibility()==View.GONE)
        {
            pathsListLayout.setVisibility(View.GONE);
            searchErrorLayout.setVisibility(View.GONE);
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
    }

    @Override
    public void showErrorMessage() {

        getPathText.setVisibility(View.GONE);
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
                searchErrorLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                pathSuggestionsLayout.setVisibility(View.GONE);
            }
        },100);


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
        intent.putExtra(IntentUtils.PATH,path.toJson());
        intent.putExtra("PATHas","HI");
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
    public void showOptionsDialog(PathPreferences pathPreferences) {
        //initialisation
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_path_options);
        dialog.setTitle("Options");
        final CheckBox transportMode0 = (CheckBox) dialog.findViewById(R.id.checkbox_transport_mean_0);
        final CheckBox transportMode1 = (CheckBox) dialog.findViewById(R.id.checkbox_transport_mean_1);
        final CheckBox transportMode2 = (CheckBox) dialog.findViewById(R.id.checkbox_transport_mean_2);
        final CheckBox transportMode3 = (CheckBox) dialog.findViewById(R.id.checkbox_transport_mean_3);
        CheckBox transportMode4 = (CheckBox) dialog.findViewById(R.id.checkbox_transport_mean_4);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogroup_sort);
        final RadioButton timeRadioButton = dialog.findViewById(R.id.radiobutton_minimum_time);
        RadioButton transfersRadioButton = dialog.findViewById(R.id.radio_button_minimum_transfers);
        RadioButton walkingTimeRadioButton = dialog.findViewById(R.id.radio_button_minimum_walking_time);
        TextView okText = dialog.findViewById(R.id.text_ok);
        TextView cancelText = dialog.findViewById(R.id.text_cancel);

        if (pathPreferences.getTransportModesNotUsed().contains(TransportMean.allTransportMeans.get(0)))
        {
            transportMode0.setChecked(false);
        }
        if (pathPreferences.getTransportModesNotUsed().contains(TransportMean.allTransportMeans.get(1)))
        {
            transportMode1.setChecked(false);
        }
        if (pathPreferences.getTransportModesNotUsed().contains(TransportMean.allTransportMeans.get(2)))
        {
            transportMode2.setChecked(false);
        }
        if (pathPreferences.getTransportModesNotUsed().contains(TransportMean.allTransportMeans.get(3)))
        {
            transportMode3.setChecked(false);
        }
        if (pathPreferences.getTransportModesNotUsed().contains(TransportMean.allTransportMeans.get(4)))
        {
            transportMode4.setChecked(false);
        }
        switch (pathPreferences.getSortPreference())
        {
            case PathPreferences.SORT_BY_MINIMUM_TIME:
                timeRadioButton.setChecked(true);
            break;
            case PathPreferences.SORT_BY_MINIMUM_TRANSFERS:
                transfersRadioButton.setChecked(true);
                break;
            case PathPreferences.SORT_BY_MINIMUM_WALKING_TIME:
                walkingTimeRadioButton.setChecked(true);
                break;
        }

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sortMethod = 0;
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.radiobutton_minimum_time : sortMethod = PathPreferences.SORT_BY_MINIMUM_TIME;
                    break;
                    case R.id.radio_button_minimum_transfers: sortMethod = PathPreferences.SORT_BY_MINIMUM_TRANSFERS;
                        break;
                    case R.id.radio_button_minimum_walking_time : sortMethod = PathPreferences.SORT_BY_MINIMUM_WALKING_TIME;
                        break;
                    case R.id.radio_button_algorithm : sortMethod = PathPreferences.SORT_BY_ALGORITHM;
                    break;
                }
                ArrayList<TransportMean> blackListedTransportModes = new ArrayList<>();
                if (!transportMode0.isChecked())
                {
                    blackListedTransportModes.add(TransportMean.allTransportMeans.get(0));
                }
                if (!transportMode1.isChecked())
                {
                    blackListedTransportModes.add(TransportMean.allTransportMeans.get(1));
                }
                if (!transportMode2.isChecked())
                {
                    blackListedTransportModes.add(TransportMean.allTransportMeans.get(2));
                }
                if (!transportMode3.isChecked())
                {
                    blackListedTransportModes.add(TransportMean.allTransportMeans.get(3));
                }
                PathPreferences newPathPreferences = new PathPreferences(sortMethod,blackListedTransportModes);
                dialog.hide();
                pathSearchPresenter.onOptionsUpdated(newPathPreferences);
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IntentUtils.RESULT_OK)
            pathSearchPresenter.onActivityResult(requestCode,
                    PathPlace.fromJson(data.getExtras().getString(IntentUtils.LOCATION)));
    }
}
