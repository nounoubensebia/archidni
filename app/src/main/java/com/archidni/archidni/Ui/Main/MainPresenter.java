package com.archidni.archidni.Ui.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ArchidniMarker;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

import java.util.ArrayList;
import java.util.logging.Handler;

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
        transportMeansSelector.selectAllTransportMeans();
        view.updateMeansSelectionLayout(transportMeansSelector);
        linesRepository = new LinesRepository();
        lines = new ArrayList<>();
        searchCoordinates = new ArrayList<>();
        view.showDrawerLayout(user);
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        /*transportMeansSelector.ToggleTransportMean(transportMeanId);
        view.updateMeansSelectionLayout(transportMeansSelector);
        view.showStationsOnMap(TransportUtils.getStationsFromLines(filteredLines()));
        populateList();*/
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
    public void onMapReady(final Context context, BoundingBox boundingBox) {
        view.setUserLocationEnabled(true);
        currentBoundingBox = boundingBox;
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
    public void onMapLongClick(Coordinate coordinate) {
        if (!errorHappened)
        {
            Place se = new Place(StringUtils.getLocationString(coordinate),
                    App.getAppContext().getString(R.string.on_map),coordinate);
            view.showLocationLayout(se,selectedLocation);
            locationLayoutVisible = true;
            view.animateCameraToLocation(coordinate);
            selectedLocation = se;
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
    public void onCameraMove(Context context,Coordinate coordinate, double zoom
            ,BoundingBox boundingBox) {
        currentBoundingBox = boundingBox;
        if (zoom<MIN_ZOOM)
        {
            if (!locationLayoutVisible)
                view.showZoomInsufficientLayout();
            view.showStationsOnMap(new ArrayList<Station>());
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
                view.showStationsOnMap(TransportUtils.getStationsFromLines(filteredLines()));
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
            }*/
        }
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
                populateList();
                view.showStationsOnMap(TransportUtils.getStationsFromLines(filteredLines()));
                searchUnderway = false;
            }

            @Override
            public void onError() {
                errorHappened = true;
                view.hideLinesLoadingLayout();
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.showSearchErrorLayout();
                    }
                },250);
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
    public void onStationMarkerClick(Station station, ArchidniMarker marker) {
            view.showLocationLayout(station,selectedLocation);
            selectedMarker = marker;
            locationLayoutVisible = true;
            view.animateCameraToLocation(station.getCoordinate());
            selectedLocation = station;
    }

    @Override
    public void onRetryClicked(final Context context, final Coordinate currentCoordinate) {
        view.hideSearchErrorLayout();
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (errorHappened)
                view.showLinesLoadingLayout();
            }
        },250);
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
        ArrayList<Station> stations = TransportUtils.getStationsFromLines(filteredLines());
        return TransportUtils.filterStations(stations,currentBoundingBox);
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

    private void populateList()
    {
        /*if (stationsSelected)
        {
            //view.showStationsOnList(filteredListStations(),userCoordinate);
        }
        else
        {
            view.showLinesOnList(TransportUtils.filterLines(filteredLines(),currentBoundingBox));
        }*/
        PopulateTask populateTask = new PopulateTask();
        populateTask.execute();
    }

    private class PopulateTask extends AsyncTask<Void,Void,Pair<Boolean,Pair<ArrayList<Line>,ArrayList<Station>>>>
    {
        @Override
        protected Pair<Boolean,Pair<ArrayList<Line>,ArrayList<Station>>> doInBackground(Void... voids) {
            if (stationsSelected)
            {
                //view.showStationsOnList(filteredListStations(),userCoordinate);
                return new Pair<>(true,new Pair<ArrayList<Line>, ArrayList<Station>>(filteredLines(),filteredListStations()));
            }
            else
            {
                return new Pair<>(false,new Pair<ArrayList<Line>, ArrayList<Station>>(filteredLines(),filteredListStations()));
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
