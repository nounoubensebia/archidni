package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private TransportMeansSelector transportMeansSelector;
    private boolean stationsSelected = true;
    private boolean locationLayoutVisible = false;
    private Place selectedLocation;
    private ArrayList<Line> lines;
    private LinesRepository linesRepository;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.transportMeansSelector = new TransportMeansSelector();
        transportMeansSelector.selectAllTransportMeans();
        view.updateMeansSelectionLayout(transportMeansSelector);
        linesRepository = new LinesRepository();
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        transportMeansSelector.ToggleTransportMean(transportMeanId);
        view.updateMeansSelectionLayout(transportMeansSelector);
    }

    @Override
    public void toggleStationsLines(boolean stationsTabbed) {
        if (stationsTabbed)
        {
            if (!stationsSelected) stationsSelected = true;
        }
        else
        {
            if (stationsSelected) stationsSelected = false;
        }
        view.updateStationsLinesLayout(stationsSelected);
    }

    @Override
    public void onSearchClicked() {
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                if (userLocation!=null)
                {
                    Place place = new Place("Ma position","Ma position",
                            userLocation);
                    view.startSearchActivity(place);
                }
                else
                {
                    view.startSearchActivity(null);
                }
            }
        });
    }

    @Override
    public void onMapReady(final Context context) {
        view.setUserLocationEnabled(true);
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                view.showLinesLoadingLayout();

                if (userLocation!=null)
                {
                    view.moveCameraToUserLocation();
                    linesRepository.getLines(context,userLocation, new LinesRepository.OnSearchCompleted() {
                        @Override
                        public void onLinesFound(ArrayList<Line> lines) {
                            view.hideLinesLoadingLayout();
                            view.showLinesOnList(lines);
                            view.showLinesOnMap(TransportUtils.getStationsFromLines(lines));
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
                else
                {
                    view.animateCameraToLocation(Coordinate.DEFAULT_LOCATION);
                }
            }
        });
    }

    @Override
    public void onMyLocationFabClick() {
        view.trackUser();
    }

    @Override
    public void onShowSlidingPanelFabClick() {
        view.showSlidingPanel();
    }

    @Override
    public void onMapLongClick(Coordinate coordinate) {
        selectedLocation = new Place(StringUtils.getLocationString(coordinate),
                App.getAppContext().getString(R.string.on_map),coordinate);
        view.showLocationLayout(selectedLocation);
        locationLayoutVisible = true;
        view.animateCameraToLocation(coordinate);
    }

    @Override
    public void onMapShortClick() {
        if (locationLayoutVisible)
        {
            view.hideLocationLayout();
            locationLayoutVisible = false;
        }
    }

    @Override
    public void onSearchPathClick() {
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                    Place userPlace = new Place("Ma position","Ma position",
                            userLocation);
                    view.startPathSearchActivity(userPlace,selectedLocation);
            }
        });
    }
}
