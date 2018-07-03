package com.archidni.archidni.Ui.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.google.android.gms.maps.model.Marker;

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
    private Marker selectedMarker;
    private static final int MIN_ZOOM = 12;
    private Coordinate mapCenterCoordinate;
    private boolean currentZoomIsInsufficient;
    private ArrayList<Coordinate> searchCoordinates;
    private boolean searchUnderway = false;
    private BoundingBox currentBoundingBox;
    private Coordinate userCoordinate;
    private boolean errorHappened = false;
    private User user;

    public MainPresenter(MainContract.View view, User user) {
        this.view = view;
        this.user = user;
        this.transportMeansSelector = new TransportMeansSelector();
        transportMeansSelector.selectAllItems();
        view.updateMeansSelectionLayout(transportMeansSelector);
        linesRepository = new LinesRepository();
        lines = new ArrayList<>();
        searchCoordinates = new ArrayList<>();
        view.showDrawerLayout(user);
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        transportMeansSelector.ToggleItem(transportMeanId);
        view.updateMeansSelectionLayout(transportMeansSelector);
        //view.showPlacesOnMap(TransportUtils.getStationsFromLines(getfilteredLines()));
        populateList();
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
        populateList();
        view.updateStationsLinesLayout(stationsSelected);
    }

    public void populateList ()
    {
        if (stationsSelected)
            view.showStationsOnList(getNearbyFilteredStations(mapCenterCoordinate),userCoordinate);
        else
            view.showLinesOnList(getfilteredLines());
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
    public void onMapReady(final Context context, BoundingBox boundingBox,Coordinate coordinate) {
        view.setUserLocationEnabled(true);
        currentBoundingBox = boundingBox;
        mapCenterCoordinate = coordinate;
        transportMeansSelector.selectAllItems();
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
                userCoordinate = userLocation;
                searchCoordinates.add(searchLocation);
                searchLines(context,searchLocation);
            }
        });
    }


    @Override
    public void onMyLocationFabClick() {
        view.trackUser();
        view.obtainUserLocation(new MainContract.OnUserLocationObtainedCallback() {
            @Override
            public void onLocationObtained(Coordinate userLocation) {
                if (userLocation!=null)
                {
                    userCoordinate = userLocation;
                }
            }
        });
    }

    @Override
    public void onShowSlidingPanelFabClick() {
        view.showSlidingPanel();
    }


    @Override
    public void onLocationMarkerCreated(Marker marker) {
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
    public void onCameraMove(Context context,Coordinate coordinate) {

        view.showStationsOnList(getNearbyFilteredStations(coordinate),userCoordinate);
        mapCenterCoordinate = coordinate;

        /*currentBoundingBox = boundingBox;
        if (zoom<MIN_ZOOM)
        {
            if (!locationLayoutVisible)
                view.showZoomInsufficientLayout();
            view.showPlacesOnMap(new ArrayList<Station>());
            currentZoomIsInsufficient = true;
            populateList();
        }
        else
        {
            currentBoundingBox = boundingBox;
            populateList();
            if (currentZoomIsInsufficient)
            {
                currentZoomIsInsufficient = false;
                view.hideZoomInsufficientLayout();
                view.showPlacesOnMap(TransportUtils.getStationsFromLines(getfilteredLines()));
            }
            boolean found = false;
            /*for (Coordinate searchCoordinate:searchCoordinates)
            {
                if (GeoUtils.distance(searchCoordinate,coordinate)<=15000)
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                if (!searchUnderway&&!errorHappened)
                {
                    if (searchCoordinates.size()>=4)
                    {
                        searchCoordinates.remove(searchCoordinates.size()-1);
                    }
                    searchCoordinates.add(coordinate);
                    int i = 0;
                    while (i<lines.size())
                    {
                        if (!lines.get(i).insideSearchCircle(searchCoordinates,15000))
                        {
                            lines.remove(i);
                        }
                        else
                        {
                            i++;
                        }
                    }
                    view.showLinesLoadingLayout();
                    searchLines(context,coordinate);
                }
            }
        }*/

    }

    private void searchLines (Context context,Coordinate coordinate)
    {
        searchUnderway = true;
        linesRepository.getLines(context,coordinate, new LinesRepository.OnSearchCompleted() {
            @Override
            public void onLinesFound(ArrayList<Line> lines) {
                errorHappened = false;
                addLines(lines);
                view.hideLinesLoadingLayout();
                view.showLinesOnList(lines);
                mapCenterCoordinate = view.getMapCenter();
                ArrayList<Place> places = new ArrayList<>();
                places.addAll(TransportUtils.getStationsFromLines(getfilteredLines()));
                populateList();
                view.showPlacesOnMap(places);
                searchUnderway = false;
            }

            @Override
            public void onError() {
                errorHappened = true;
                view.hideLinesLoadingLayout();
                view.showSearchErrorLayout();
            }
        });
    }

    private void addLines(ArrayList<Line> lines)
    {
        for (Line line:lines)
        {
            if (!TransportUtils.containsLine(line.getId(),this.lines))
            {
                this.lines.add(line);
            }
        }
    }

    @Override
    public void onStationMarkerClick(Station station, Marker marker) {
            view.showLocationLayout(station,selectedLocation,marker);
            selectedMarker = marker;
            locationLayoutVisible = true;
            view.animateCameraToLocation(station.getCoordinate());
            selectedLocation = station;
    }

    @Override
    public void onParkingMarkerClick(Parking parking, Marker marker) {
        view.showLocationLayout(parking,selectedLocation,marker);
        selectedMarker = marker;
        locationLayoutVisible = true;
        view.animateCameraToLocation(parking.getCoordinate());
        selectedLocation = parking;
    }

    @Override
    public void onRetryClicked(final Context context, final Coordinate currentCoordinate) {
        view.hideSearchErrorLayout();
        view.showLinesLoadingLayout();
        searchLines(context,currentCoordinate);
    }

    @Override
    public void onStationFabClick() {
        view.startStationActivity((Station)selectedLocation);
    }

    @Override
    public void onStationItemClick(Station station) {
        view.startStationActivity(station);
    }

    @Override
    public void onLineItemClicked(Line line) {
        view.startLineActivity(line);
    }

    @Override
    public void onLinesStationsFindClick() {
        view.startLinesStationsActivity();
    }

    @Override
    public void onLogoutClick() {
        view.logoutUser();
    }

    @Override
    public void onFavoritesClick() {
        view.startFavoritesActivity();
    }

    @Override
    public void onStop(Context context) {

        //linesRepository.cancelAllRequests(context);
    }

    private ArrayList<Station> filteredListStations ()
    {
        ArrayList<Station> stations = TransportUtils.getStationsFromLines(getfilteredLines());
        return stations;
    }

    private ArrayList<Station> getNearbyFilteredStations (Coordinate coordinate)
    {
        return TransportUtils.getNearbyStations(coordinate,filteredListStations(),1000);
    }

    private ArrayList<Line> getfilteredLines()
    {
        ArrayList<Line> filteredLines = new ArrayList<>();
        for (Line line:lines)
        {
            if (transportMeansSelector.isItemSelected(line.getTransportMean().getId()))
            {
                filteredLines.add(line);
            }
        }
        return filteredLines;
    }



    private class PopulateTask extends AsyncTask<Void,Void,Pair<Boolean,Pair<ArrayList<Line>,ArrayList<Station>>>>
    {
        @Override
        protected Pair<Boolean,Pair<ArrayList<Line>,ArrayList<Station>>> doInBackground(Void... voids) {
            if (stationsSelected)
            {
                //view.showStationsOnList(filteredListStations(),userCoordinate);
                return new Pair<>(true,new Pair<ArrayList<Line>, ArrayList<Station>>(getfilteredLines(),filteredListStations()));
            }
            else
            {
                return new Pair<>(false,new Pair<ArrayList<Line>, ArrayList<Station>>(getfilteredLines(),filteredListStations()));
            }

        }

        @Override
        protected void onPostExecute(Pair<Boolean, Pair<ArrayList<Line>, ArrayList<Station>>> booleanPairPair) {
            if (booleanPairPair.first)
                view.showStationsOnList(booleanPairPair.second.second,userCoordinate);
            else
                view.showLinesOnList(booleanPairPair.second.first);
        }
    }
}
