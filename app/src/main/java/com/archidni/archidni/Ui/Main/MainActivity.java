package com.archidni.archidni.Ui.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.archidni.archidni.Data.GeoRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Ui.Search.SearchActivity;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.R;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    MainContract.Presenter presenter;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.text_transport_mean_0)
    TextView transportMean0Text;
    @BindView(R.id.text_transport_mean_1)
    TextView transportMean1Text;
    @BindView(R.id.text_transport_mean_2)
    TextView transportMean2Text;
    @BindView(R.id.text_transport_mean_3)
    TextView transportMean3Text;
    @BindView(R.id.text_stations)
    TextView stationsText;
    @BindView(R.id.text_lines)
    TextView linesText;
    @BindView(R.id.layout_lines)
    View linesLayout;
    @BindView(R.id.layout_stations)
    View stationsLayout;
    @BindView(R.id.layout_search)
    View searchLayout;
    @BindView(R.id.fab_my_location)
    View myLocationFab;
    @BindView(R.id.fab_show_sliding_panel)
    View showSlidingUpPanelFab;
    @BindView(R.id.container)
    SlidingUpPanelLayout container;
    @BindView(R.id.layout_location)
    View locationLayout;
    @BindView(R.id.text_main)
    TextView mainText;
    @BindView(R.id.text_secondary)
    TextView secondaryText;
    @BindView(R.id.text_duration_distance)
    TextView durationDistanceText;
    ArchidniMap archidniMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoibm91bm91OTYiLCJhIjoiY2o0Z29mMXNsMDVoazMzbzI1NTJ1MmRqbCJ9.CXczOhM2eznwR0Mv6h2Pgg");
        setContentView(R.layout.activity_main);
        initViews(savedInstanceState);
        presenter = new MainPresenter(this);
    }

    private void initViews(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        archidniMap = new ArchidniMap(mapView, savedInstanceState, new ArchidniMap.OnMapReadyCallback() {
            @Override
            public void onMapReady() {
                presenter.onMapReady();
                archidniMap.setOnMapLongClickListener(new ArchidniMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(Coordinate coordinate) {
                        presenter.onMapLongClick(coordinate);
                    }
                });
                archidniMap.setOnMapShortClickListener(new ArchidniMap.OnMapShortClickListener() {
                    @Override
                    public void onMapShortClick(Coordinate coordinate) {
                        presenter.onMapShortClick();
                    }
                });
            }
        });
        transportMean0Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleTransportMean(0);
            }
        });
        transportMean1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleTransportMean(1);
            }
        });
        transportMean2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleTransportMean(2);
            }
        });
        transportMean3Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleTransportMean(3);
            }
        });
        stationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleStationsLines(true);
            }
        });
        linesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toggleStationsLines(false);
            }
        });
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSearchClicked();
            }
        });

        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onMyLocationFabClick();
            }
        });
        showSlidingUpPanelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onShowSlidingPanelFabClick();
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
    public void updateMeansSelectionLayout(TransportMeansSelector transportMeansSelector) {
        if (transportMeansSelector.isTransportMeanSelected(0))
        {
            ViewUtils.changeTextViewState(this,transportMean0Text,
                    TransportMean.allTransportMeans.get(0).getIconEnabled(),
                    R.color.color_transport_mean_selected_0
                    ,ViewUtils.DIRECTION_UP);
        }
        else
        {
            ViewUtils.changeTextViewState(this,transportMean0Text,
                    TransportMean.allTransportMeans.get(0).getIconDisabled(),
                    R.color.color_transport_mean_not_selected
                    ,ViewUtils.DIRECTION_UP);
        }
        if (transportMeansSelector.isTransportMeanSelected(1))
        {
            ViewUtils.changeTextViewState(this,transportMean1Text,
                    TransportMean.allTransportMeans.get(1).getIconEnabled(),
                    R.color.color_transport_mean_selected_1
                    ,ViewUtils.DIRECTION_UP);
        }
        else
        {
            ViewUtils.changeTextViewState(this,transportMean1Text,
                    TransportMean.allTransportMeans.get(1).getIconDisabled(),
                    R.color.color_transport_mean_not_selected
                    ,ViewUtils.DIRECTION_UP);
        }
        if (transportMeansSelector.isTransportMeanSelected(2))
        {
            ViewUtils.changeTextViewState(this,transportMean2Text,
                    TransportMean.allTransportMeans.get(2).getIconEnabled(),
                    R.color.color_transport_mean_selected_2
                    ,ViewUtils.DIRECTION_UP);
        }
        else
        {
            ViewUtils.changeTextViewState(this,transportMean2Text,
                    TransportMean.allTransportMeans.get(2).getIconDisabled(),
                    R.color.color_transport_mean_not_selected
                    ,ViewUtils.DIRECTION_UP);
        }
        if (transportMeansSelector.isTransportMeanSelected(3))
        {
            ViewUtils.changeTextViewState(this,transportMean3Text,
                    TransportMean.allTransportMeans.get(3).getIconEnabled(),
                    R.color.color_transport_mean_selected_3
                    ,ViewUtils.DIRECTION_UP);
        }
        else
        {
            ViewUtils.changeTextViewState(this,transportMean3Text,
                    TransportMean.allTransportMeans.get(3).getIconDisabled(),
                    R.color.color_transport_mean_not_selected
                    ,ViewUtils.DIRECTION_UP);
        }
    }

    @Override
    public void updateStationsLinesLayout(boolean stationsSelected) {
        if (stationsSelected)
        {
            ViewUtils.changeTextViewState(this,
                    stationsText,
                    R.drawable.ic_station_enabled,
                    R.color.colorGreen,
                    ViewUtils.DIRECTION_RIGHT);
            ViewUtils.changeTextViewState(this,
                    linesText,
                    R.drawable.ic_line_disabled,
                    R.color.black,
                    ViewUtils.DIRECTION_RIGHT);
        }
        else
        {
            ViewUtils.changeTextViewState(this,
                    stationsText,
                    R.drawable.ic_station_disabled,
                    R.color.black,
                    ViewUtils.DIRECTION_RIGHT);
            ViewUtils.changeTextViewState(this,
                    linesText,
                    R.drawable.ic_line_enabled,
                    R.color.colorGreen,
                    ViewUtils.DIRECTION_RIGHT);
        }

    }

    @Override
    public void startSearchActivity(Place userLocation) {
        Intent intent = new Intent(this, SearchActivity.class);
        String json = new Gson().toJson(userLocation);
        intent.putExtra(IntentUtils.LOCATION,json);
        intent.putExtra(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE,
                IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_DESTINATION);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showLocationLayout(Place place) {
        container.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mainText.setText(place.getMainText());
        secondaryText.setText(place.getSecondaryText());
        slideOutSearchText();
        if (archidniMap.getUserLocation()!=null)
        {
            durationDistanceText.setText(StringUtils
                    .getTextFromDistance(GeoUtils.distance(archidniMap.getUserLocation(),
                            place.getCoordinate()))+", "+
                    StringUtils.getTextFromDuration(GeoUtils.getOnFootDuration(archidniMap.getUserLocation(),
                            place.getCoordinate()))+" de marche");

        }
        else
        {
            durationDistanceText.setText("");
        }
        locationLayout.setVisibility(View.VISIBLE);
        showSlidingUpPanelFab.setVisibility(View.GONE);
        myLocationFab.setVisibility(View.GONE);
        container.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        },250);
    }

    @Override
    public void hideLocationLayout() {
        container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        myLocationFab.setVisibility(View.VISIBLE);
        showSlidingUpPanelFab.setVisibility(View.VISIBLE);
        locationLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slideInSearchText();
            }
        },250);

    }

    @Override
    public void moveCameraToUserLocation() {
        archidniMap.moveCamera(archidniMap.getUserLocation(),15);
    }

    @Override
    public void moveCameraToLocation(Coordinate coordinate) {

    }

    @Override
    public void obtainUserLocation(
            MainContract.OnUserLocationObtainedCallback onUserLocationObtainedCallback) {
        onUserLocationObtainedCallback.onLocationObtained(archidniMap.getUserLocation());
    }


    @Override
    public void trackUser() {
        archidniMap.trackUser();
    }

    @Override
    public void showSlidingPanel() {
        container.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void setUserLocationEnabled(boolean enable) {
        archidniMap.setMyLocationEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        if (container.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void slideInSearchText() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_top);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        searchLayout.startAnimation(animation);
    }

    private void slideOutSearchText() {
        searchLayout.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_top);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        searchLayout.startAnimation(animation);
    }
}
