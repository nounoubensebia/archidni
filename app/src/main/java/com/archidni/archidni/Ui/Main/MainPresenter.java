package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ArchidniMarker;
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
    private ArchidniMarker selectedMarker;
    private static final int MIN_ZOOM = 12;
    private boolean currentZoomIsInsufficient;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.transportMeansSelector = new TransportMeansSelector();
        transportMeansSelector.selectAllTransportMeans();
        view.updateMeansSelectionLayout(transportMeansSelector);
        linesRepository = new LinesRepository();
        lines = new ArrayList<>();
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        transportMeansSelector.ToggleTransportMean(transportMeanId);
        view.updateMeansSelectionLayout(transportMeansSelector);
        view.showLinesOnMap(TransportUtils.getStationsFromLines(filteredLines()));
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
        transportMeansSelector.selectAllTransportMeans();
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                view.showLinesLoadingLayout();
                Coordinate searchLocation;
                if (userLocation!=null)
                {
                    view.moveCameraToUserLocation();
                    searchLocation = userLocation;
                }
                else
                {
                    searchLocation = Coordinate.DEFAULT_LOCATION;
                    view.animateCameraToLocation(Coordinate.DEFAULT_LOCATION);
                }

                linesRepository.getLines(context,searchLocation, new LinesRepository.OnSearchCompleted() {
                    @Override
                    public void onLinesFound(ArrayList<Line> lines) {
                        MainPresenter.this.lines = lines;
                        view.hideLinesLoadingLayout();
                        view.showLinesOnList(lines);
                        view.showLinesOnMap(TransportUtils.getStationsFromLines(filteredLines()));
                    }

                    @Override
                    public void onError() {

                    }
                });
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
        if (selectedLocation == null)
        {
            selectedLocation = new Place(StringUtils.getLocationString(coordinate),
                    App.getAppContext().getString(R.string.on_map),coordinate);
            view.showLocationLayout(selectedLocation);
            locationLayoutVisible = true;
            view.animateCameraToLocation(coordinate);
        }
    }

    @Override
    public void onLocationMarkerCreated(ArchidniMarker marker) {
        selectedMarker = marker;
    }

    @Override
    public void onMapShortClick() {
        if (locationLayoutVisible)
        {
            view.hideLocationLayout(selectedMarker);
            locationLayoutVisible = false;
            selectedMarker = null;
            selectedLocation = null;
            if (currentZoomIsInsufficient)
            {
                view.showZoomInsufficientLayout();
            }
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


    @Override
    public void onCameraMove(Coordinate coordinate, double zoom) {
        if (zoom<MIN_ZOOM)
        {
            if (!locationLayoutVisible)
                view.showZoomInsufficientLayout();
            view.showLinesOnMap(new ArrayList<Station>());
            currentZoomIsInsufficient = true;
        }
        else
        {
            if (currentZoomIsInsufficient)
            {
                currentZoomIsInsufficient = false;
                view.hideZoomInsufficientLayout();
                view.showLinesOnMap(TransportUtils.getStationsFromLines(filteredLines()));
            }
        }
    }

    @Override
    public void onStationMarkerClick(Station station, ArchidniMarker marker) {
        if (selectedLocation==null)
        {
            view.showLocationLayout(station);
            selectedMarker = marker;
            locationLayoutVisible = true;
            view.animateCameraToLocation(station.getCoordinate());
            selectedLocation = station;
        }
    }

    private ArrayList<Line> filteredLines ()
    {
        ArrayList<Line> filteredLines = new ArrayList<>();
        for (Line line:lines)
        {
            if (transportMeansSelector.isTransportMeanSelected(line.getTransportMean().getId()))
            {
                filteredLines.add(line);
            }
        }
        return filteredLines;
    }
}
