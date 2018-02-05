package com.archidni.archidni.Ui.Main;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

/**
 * Created by noure on 02/02/2018.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private TransportMeansSelector transportMeansSelector;
    private boolean stationsSelected = true;
    private boolean locationLayoutVisible = false;
    private Place selectedLocation;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.transportMeansSelector = new TransportMeansSelector();
        transportMeansSelector.selectAllTransportMeans();
        view.updateMeansSelectionLayout(transportMeansSelector);
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
    public void onMapReady() {
        view.setUserLocationEnabled(true);
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                if (userLocation!=null)
                {
                    view.moveCameraToUserLocation();
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
