package com.archidni.archidni.Ui.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationLines;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.TimeMonitor;
import com.archidni.archidni.UiUtils.ArchidniClusterItem;
import com.archidni.archidni.UiUtils.SelectorItem;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by noure on 02/02/2018.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private TransportMeansSelector transportMeansSelector;
    private int selectedItem;
    private boolean locationLayoutVisible = false;
    private PathPlace selectedLocation;
    private ArrayList<Line> lines;
    private ArrayList<MainActivityPlace> interestPlaces;
    private LinesAndPlacesRepository linesAndPlacesRepository;
    private Marker selectedMarker;
    private ArchidniClusterItem selectedClusterItem;
    private Coordinate mapCenterCoordinate;
    private Coordinate userCoordinate;
    private TimeMonitor timeMonitor;
    private User user;
    private ArrayList<StationLines> stationLinesArrayList;
    private LatLngBounds currentLatLngBounds;

    public static final int STATIONS_SELECTED = 0;
    public static final int LINES_SELECTED = 1;
    public static final int INTERESTS_SELECTED = 2;

    public MainPresenter(MainContract.View view, User user) {
        this.view = view;
        this.user = user;
        this.transportMeansSelector = new TransportMeansSelector();
        transportMeansSelector.selectAllItems();
        view.updateMeansSelectionLayout(transportMeansSelector);
        linesAndPlacesRepository = new LinesAndPlacesRepository();
        lines = new ArrayList<>();
        view.showDrawerLayout(user);
        interestPlaces = new ArrayList<>();
        timeMonitor = TimeMonitor.initTimeMonitor();
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        TransportMeansSelector transportMeansSelector = new TransportMeansSelector();
        for (SelectorItem selectorItem:SelectorItem.allItems)
        {
            if (this.transportMeansSelector.isItemSelected(selectorItem.getId()))
            {
                transportMeansSelector.ToggleItem(selectorItem.getId());
            }
        }
        this.transportMeansSelector.ToggleItem(transportMeanId);
        view.updateMeansSelectionLayout(this.transportMeansSelector);
        //view.showPlacesOnMap(TransportUtils.getStationsFromLines(getfilteredLines()));
        view.updatePlacesOnMap(getFilteredPlaces(),this.transportMeansSelector,transportMeansSelector);
        populateList();
    }

    private ArrayList<MainActivityPlace> getFilteredPlaces ()
    {
        ArrayList<MainActivityPlace> filtredPlaces = new ArrayList<>();
        for (MainActivityPlace place:interestPlaces)
        {
            if (place instanceof Parking && transportMeansSelector.isItemSelected(5))
            {
                filtredPlaces.add(place);
            }
        }

        filtredPlaces.addAll(TransportUtils.getStationsFromLines(getfilteredLines()));
        return filtredPlaces;
    }

    @Override
    public void toggleStationsLines(int selectedItem) {
        if (selectedItem!=this.selectedItem)
        {
            this.selectedItem = selectedItem;
        }
        populateList();
        view.updateStationsLinesLayout(selectedItem);
    }

    public void populateList ()
    {
        if (selectedItem == STATIONS_SELECTED)
            view.showPlacesOnList(getMapAreaStations(mapCenterCoordinate),userCoordinate);
        if (selectedItem == LINES_SELECTED)
            view.showLinesOnList(getNearbyFilteredLines(mapCenterCoordinate));
        if (selectedItem == INTERESTS_SELECTED)
        {
            ArrayList<Parking> parkings = new ArrayList<>();
            for (MainActivityPlace place:interestPlaces)
            {
                if (place instanceof Parking && transportMeansSelector.isItemSelected(SelectorItem.PARKING_ID))
                {
                    parkings.add((Parking)place);
                }
            }
            view.showPlacesOnList(parkings,userCoordinate);
        }
    }

    private void sortStations (ArrayList<Station> stations, final Coordinate coordinate)
    {
        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station station, Station t1) {
                return (GeoUtils.distance(station.getCoordinate(),coordinate)-
                GeoUtils.distance(t1.getCoordinate(),coordinate));
            }
        });
    }

    @Override
    public void onSearchClicked() {
        if (userCoordinate!=null)
        {
            PathPlace place = new PathPlace("Ma positoin",userCoordinate);
            view.startSearchActivity(place);
        }
        else
        {
            view.startSearchActivity(null);
        }
    }

    @Override
    public void onMapReady(final Context context, LatLngBounds latLngBounds,Coordinate coordinate) {
        view.setUserLocationEnabled(true);
        mapCenterCoordinate = coordinate;
        transportMeansSelector.selectAllItems();
        view.showLinesLoadingLayout();
        this.currentLatLngBounds = latLngBounds;
        Coordinate searchLocation;
        if (userCoordinate!=null)
        {
            view.moveCameraToLocation(userCoordinate);
            searchLocation = userCoordinate;
        }
        else
        {
            searchLocation = Coordinate.DEFAULT_LOCATION;
            view.moveCameraToLocation(Coordinate.DEFAULT_LOCATION);
        }

        searchLines(context,searchLocation);

    }


    @Override
    public void onMyLocationFabClick() {
        if (userCoordinate!=null)
        {
            view.animateCameraToLocation(userCoordinate);
        }
    }

    @Override
    public void onUserLocationUpdated(Coordinate userLocation) {
        this.userCoordinate = userLocation;
        populateList();
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
            view.hideLocationLayout(selectedMarker,selectedClusterItem);
            locationLayoutVisible = false;
            selectedMarker = null;
            selectedLocation = null;
        }
    }

    @Override
    public void onSearchPathClick() {
        PathPlace userPlace = null;
        if (userCoordinate!=null)
        {
            userPlace = new PathPlace("Ma position",userCoordinate);
        }
        view.startPathSearchActivity(userPlace,selectedLocation);

    }


    @Override
    public void onCameraMove(Context context,Coordinate coordinate,float zoom,LatLngBounds latLngBounds) {
        currentLatLngBounds = latLngBounds;
        populateList();
        //view.updatePlacesOnMap(getFilteredPlaces(),transportMeansSelector);
        //view.showPlacesOnMap(getFilteredPlaces(),transportMeansSelector);

        mapCenterCoordinate = coordinate;

    }

    private void searchLines (Context context,Coordinate coordinate)
    {
        linesAndPlacesRepository.getLinesAndPlaces(context,new LinesAndPlacesRepository.OnLinesAndPlacesSearchCompleted() {
            @Override
            public void onFound(final ArrayList<Line> lines, final ArrayList<MainActivityPlace> places) {
                mapCenterCoordinate = view.getMapCenter();
                @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,ArrayList<MainActivityPlace>> asyncTask = new AsyncTask<Void, Void, ArrayList<MainActivityPlace>>() {
                    @Override
                    protected ArrayList<MainActivityPlace> doInBackground(Void... voids) {
                        TimeMonitor timeMonitor = TimeMonitor.initTimeMonitor();
                        addLines(lines);
                        MainPresenter.this.interestPlaces.addAll(places);
                        ArrayList<MainActivityPlace> placesToShow = new ArrayList<>();
                        placesToShow.addAll(places);
                        placesToShow.addAll(TransportUtils.getStationsFromLines(getfilteredLines()));
                        TimeMonitor stationsLinesList = TimeMonitor.initTimeMonitor();
                        stationLinesArrayList = TransportUtils.getStationLines(lines);
                        Log.i("StationsArrayList" ,stationsLinesList.getElapsedTime()+"");
                        Log.i("LOGIC TIME",timeMonitor.getElapsedTime()+"");
                        return placesToShow;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<MainActivityPlace> aVoid) {
                        TimeMonitor timeMonitor = TimeMonitor.initTimeMonitor();
                        populateList();
                        Log.i("POPULATE LIST TIME" ,timeMonitor.getElapsedTime()+"");
                        view.hideLinesLoadingLayout();
                        populateList();
                        TimeMonitor timeMonitor1 = TimeMonitor.initTimeMonitor();
                        view.showPlacesOnMap(aVoid,transportMeansSelector);
                        Log.i("MAP TIME",timeMonitor1.getElapsedTime()+"");
                    }
                };
                asyncTask.execute();
            }

            @Override
            public void onError() {
                view.hideLinesLoadingLayout();
                view.showSearchErrorLayout();
            }
        });
    }

    private void addLines(ArrayList<Line> lines)
    {
        this.lines.addAll(lines);
    }

    @Override
    public void onStationMarkerClick(Station station, Marker marker,ArchidniClusterItem archidniClusterItem) {
            view.showLocationLayout(station,selectedMarker,(MainActivityPlace)selectedLocation,marker);
            selectedMarker = marker;
            this.selectedClusterItem = archidniClusterItem;
            locationLayoutVisible = true;
            view.animateCameraToLocation(station.getCoordinate());
            selectedLocation = station;
    }

    @Override
    public void onParkingMarkerClick(Parking parking, Marker marker,ArchidniClusterItem archidniClusterItem) {
        view.showLocationLayout(parking,selectedMarker,(MainActivityPlace)selectedLocation,marker);
        selectedMarker = marker;
        locationLayoutVisible = true;
        this.selectedClusterItem = archidniClusterItem;
        view.animateCameraToLocation(parking.getCoordinate());
        selectedLocation = parking;
    }

    @Override
    public void onRetryClicked(final Context context, final Coordinate currentCoordinate) {
        view.hideSearchErrorLayout();
        view.showLinesLoadingLayout();
        searchLines(context,currentCoordinate);
    }

    /*@Override
    public void onStationFabClick() {
        view.startStationActivity((Station)selectedLocation);
    }*/


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
    public void onPlaceClick() {
        if (selectedLocation instanceof Station)
        {
            view.startStationActivity((Station)selectedLocation);
        }
        else
        {
            view.startParkingActivity((Parking)selectedLocation);
        }
    }

    @Override
    public void onFavoritesClick() {
        view.startFavoritesActivity();
    }

    @Override
    public void onStop(Context context) {

        //linesAndPlacesRepository.cancelAllRequests(context);
    }

    @Override
    public void onParkingClick(Parking parking) {
        view.startParkingActivity(parking);
    }

    @Override
    public void onFirstLocationCaptured(Coordinate coordinate) {
        userCoordinate = coordinate;
        if (timeMonitor.getElapsedTime()<1000)
        {
            view.moveCameraToLocation(coordinate);
        }
    }

    @Override
    public void onMapLoaded(Coordinate coordinate, LatLngBounds latLngBounds, double zoom) {
        mapCenterCoordinate = coordinate;
        this.currentLatLngBounds = latLngBounds;
        populateList();
    }

    private ArrayList<Station> getFilteredStations()
    {
        ArrayList<Station> stations = TransportUtils.getStationsFromLines(getfilteredLines());
        return stations;
    }

    private ArrayList<Station> getMapAreaStations(Coordinate coordinate)
    {
        ArrayList<Station> filteredStations = getFilteredStations();
        ArrayList<Station> nearbyStations = TransportUtils.getNearbyStations(coordinate,
                filteredStations,2000);
        ArrayList<Station> areaStations = new ArrayList<>();
        for (Station station : nearbyStations)
        {
            if (station.getCoordinate().isInsideBounds(currentLatLngBounds))
            {
                areaStations.add(station);
            }
        }
        sortStations(areaStations,coordinate);
        return areaStations;
    }

    private ArrayList<Line> getNearbyFilteredLines (Coordinate coordinate)
    {
        ArrayList<Line> nearbyLines = new ArrayList<>();
        ArrayList<Station> stations = getMapAreaStations(coordinate);
        for (StationLines stationLines:stationLinesArrayList)
        {
            for (Station station : stations)
            {
                if (station.getId()==stationLines.getStation().getId())
                {
                    nearbyLines.addAll(stationLines.getLines());
                }
            }
        }
        Set<Line> lineSet = new HashSet<>(nearbyLines);
        nearbyLines.clear();
        nearbyLines.addAll(lineSet);
        sortLines(nearbyLines);
        return nearbyLines;
    }

    private void sortLines (ArrayList<Line> lines)
    {
        Collections.sort(lines, new Comparator<Line>() {
            @Override
            public int compare(Line line, Line t1) {
                int firstLineTransportMean = line.getTransportMean().getId();
                if (line.getTransportMean().getId()== TransportMean.ID_TRAMWAY)
                {
                    firstLineTransportMean = -1;
                }

                if (line.getTransportMean().getId()== TransportMean.ID_TELEPHERIQUE)
                {
                    firstLineTransportMean = -2;
                }
                int secondTransportMean = t1.getTransportMean().getId();
                if (t1.getTransportMean().getId()== TransportMean.ID_TRAMWAY)
                {
                    secondTransportMean = -1;
                }

                if (t1.getTransportMean().getId()== TransportMean.ID_TELEPHERIQUE)
                {
                    secondTransportMean = -2;
                }

                return (firstLineTransportMean-secondTransportMean);
            }
        });
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



}
