package com.archidni.archidni.Ui.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
import com.archidni.archidni.Ui.Search.SearchActivity;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.R;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.UiUtils.ArchidniMarker;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

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
    @BindView(R.id.layout_get_path)
    View getPathLayout;
    @BindView(R.id.layout_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.image_open_drawer)
    View openDrawerImage;
    @BindView(R.id.layout_search_underway)
    View searchUnderwayLayout;
    @BindView(R.id.location_fab)
    ImageView placeFab;
    @BindView(R.id.image_transport_mean_icon)
    ImageView locationIcon;
    ArchidniMap archidniMap;
    private boolean drawerOpened;


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
                presenter.onMapReady(MainActivity.this);
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
                archidniMap.setOnMarkerClickListener(new ArchidniMap.OnMarkerClickListener() {
                    @Override
                    public void onMarkerClick(ArchidniMarker archidniMarker) {
                        if (archidniMarker.getTag()!=null)
                        {
                            Station station = (Station) archidniMarker.getTag();
                            presenter.onStationMarkerClickListener(station,archidniMarker);
                        }
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
        getPathLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSearchPathClick();
            }
        });
        openDrawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
                container.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                drawerOpened = true;
                myLocationFab.setVisibility(View.GONE);
                showSlidingUpPanelFab.setVisibility(View.GONE);
                getPathLayout.setVisibility(View.GONE);
                slideOutSearchText();
            }
        });
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerOpened = false;
                container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                getPathLayout.setVisibility(View.VISIBLE);
                myLocationFab.setVisibility(View.VISIBLE);
                showSlidingUpPanelFab.setVisibility(View.VISIBLE);
                slideInSearchText();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

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
                    StringUtils.getTextFromDuration(GeoUtils.getOnFootDuration(archidniMap
                                    .getUserLocation(),
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
        if (!(place instanceof Station))
        {
            locationIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_marker_green_24dp));
            getPathLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    R.color.colorGreen));
            ArchidniMarker archidniMarker = archidniMap.addMarker(place.getCoordinate(),
                    R.drawable.ic_marker_green_24dp);
            presenter.onLocationMarkerCreated(archidniMarker);
        }
        else
        {
            Station station = (Station) place;
            locationIcon.setImageDrawable(ContextCompat.getDrawable(this,
                    station.getTransportMean().getMarkerIcon()));
            getPathLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                    station.getTransportMean().getColor()));
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab);
            animation.setRepeatCount(Animation.INFINITE);
            placeFab.setImageDrawable(ContextCompat.getDrawable(this,
                    station.getTransportMean().getFabIcon()));
            placeFab.startAnimation(animation);
            placeFab.setVisibility(View.VISIBLE);
            archidniMap.changeMarkerIcon(R.drawable.marker_selected,station);
        }
    }

    @Override
    public void hideLocationLayout(ArchidniMarker archidniMarker) {
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
        if (archidniMarker.getTag()!=null)
        {
            Station station = (Station) archidniMarker.getTag();
            archidniMap.changeMarkerIcon(station.getTransportMean().getMarkerIcon(),
                    archidniMarker.getTag());
            placeFab.clearAnimation();
            placeFab.setVisibility(View.GONE);
        }
        else
        {
            archidniMap.removeMarker(null);
        }
    }

    @Override
    public void startPathSearchActivity(Place origin, Place destination) {
        Intent intent = new Intent(this, PathSearchActivity.class);
        intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,origin.toJson());
        intent.putExtra(IntentUtils.PATH_SEARCH_DESTINATION,destination.toJson());
        startActivity(intent);
    }

    @Override
    public void showLinesLoadingLayout() {
        searchUnderwayLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLinesLoadingLayout() {
        searchUnderwayLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLinesOnMap(ArrayList<Station> stations) {
        for(Station station:stations)
        {
            archidniMap.prepareMarker(station.getCoordinate(),
                    station.getTransportMean().getMarkerIcon(),station);
        }
        archidniMap.addPreparedAnnotations();
    }

    @Override
    public void showLinesOnList(ArrayList<Line> lines) {

    }


    @Override
    public void moveCameraToUserLocation() {
        archidniMap.moveCamera(archidniMap.getUserLocation(),15);
    }

    @Override
    public void animateCameraToLocation(Coordinate coordinate) {
        archidniMap.animateCamera(coordinate,15,250);
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
            if (drawerOpened)
            {
                drawerLayout.closeDrawer(Gravity.START);
            }
            else
            {
                super.onBackPressed();
            }
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
