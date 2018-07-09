package com.archidni.archidni.Ui.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.LocationListener;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Interests.ParkingType;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationLines;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.UiUtils.SelectorItem;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
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
    private Place selectedLocation;
    private ArrayList<Line> lines;
    private ArrayList<Place> interestPlaces;
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
    private ArrayList<StationLines> stationLinesArrayList;

    public static final int STATIONS_SELECTED = 0;
    public static final int LINES_SELECTED = 1;
    public static final int INTERESTS_SELECTED = 2;

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
        interestPlaces = new ArrayList<>();
    }

    @Override
    public void toggleTransportMean(int transportMeanId) {
        transportMeansSelector.ToggleItem(transportMeanId);
        view.updateMeansSelectionLayout(transportMeansSelector);
        //view.showPlacesOnMap(TransportUtils.getStationsFromLines(getfilteredLines()));
        view.updatePlacesOnMap(getFilteredPlaces(),transportMeansSelector);
        populateList();
    }

    private ArrayList<Place> getFilteredPlaces ()
    {
        ArrayList<Place> filtredPlaces = new ArrayList<>();
        for (Place place:interestPlaces)
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
            view.showPlacesOnList(getNearbyFilteredStations(mapCenterCoordinate),userCoordinate);
        if (selectedItem == LINES_SELECTED)
            view.showLinesOnList(getNearbyFilteredLines(mapCenterCoordinate));
        if (selectedItem == INTERESTS_SELECTED)
        {
            ArrayList<Parking> parkings = new ArrayList<>();
            for (Place place:interestPlaces)
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
            Place place = new Place("Ma position","Ma position",
                    userCoordinate);
            view.startSearchActivity(place);
        }
        else
        {
            view.startSearchActivity(null);
        }
    }

    @Override
    public void onMapReady(final Context context, BoundingBox boundingBox,Coordinate coordinate) {
        view.setUserLocationEnabled(true);
        currentBoundingBox = boundingBox;
        mapCenterCoordinate = coordinate;
        transportMeansSelector.selectAllItems();
        view.showLinesLoadingLayout();
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
        Place userPlace = null;
        if (userCoordinate!=null)
        {
            userPlace = new Place("Ma position","Ma position",
                    userCoordinate);
        }
        view.startPathSearchActivity(userPlace,selectedLocation);

    }


    @Override
    public void onCameraMove(Context context,Coordinate coordinate) {

        populateList();
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
        /*linesRepository.getLines(context,coordinate, new LinesRepository.OnSearchCompleted() {
            @Override
            public void onLinesFound(ArrayList<Line> lines) {
                errorHappened = false;
                addLines(lines);
                view.hideLinesLoadingLayout();
                view.showLinesOnList(lines);
                mapCenterCoordinate = view.getMapCenter();
                ArrayList<Place> interestPlaces = new ArrayList<>();
                interestPlaces.addAll(TransportUtils.getStationsFromLines(getfilteredLines()));
                populateList();
                view.showPlacesOnMap(interestPlaces);
                searchUnderway = false;
            }

            @Override
            public void onError() {
                errorHappened = true;
                view.hideLinesLoadingLayout();
                view.showSearchErrorLayout();
            }
        });*/
        linesRepository.getLines(context, coordinate, new LinesRepository.OnLinesAndPlacesSearchCompleted() {
            @Override
            public void onFound(ArrayList<Line> lines, ArrayList<Place> places) {
                errorHappened = false;
                addLines(lines);
                view.hideLinesLoadingLayout();
                view.showLinesOnList(lines);
                mapCenterCoordinate = view.getMapCenter();
                MainPresenter.this.interestPlaces.addAll(places);
                ArrayList<Place> placesToShow = new ArrayList<>();
                placesToShow.addAll(places);
                placesToShow.addAll(TransportUtils.getStationsFromLines(getfilteredLines()));
                populateList();
                view.showPlacesOnMap(placesToShow,transportMeansSelector);
                searchUnderway = false;
                stationLinesArrayList = TransportUtils.getStationLines(lines);
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
            view.showLocationLayout(station,selectedMarker,marker);
            selectedMarker = marker;
            locationLayoutVisible = true;
            view.animateCameraToLocation(station.getCoordinate());
            selectedLocation = station;
    }

    @Override
    public void onParkingMarkerClick(Parking parking, Marker marker) {
        view.showLocationLayout(parking,selectedMarker,marker);
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

        //linesRepository.cancelAllRequests(context);
    }

    @Override
    public void onParkingClick(Parking parking) {
        view.startParkingActivity(parking);
    }

    private ArrayList<Station> filteredListStations ()
    {
        ArrayList<Station> stations = TransportUtils.getStationsFromLines(getfilteredLines());
        return stations;
    }

    private ArrayList<Station> getNearbyFilteredStations (Coordinate coordinate)
    {
        ArrayList<Station> nearbyStations = TransportUtils.getNearbyStations(coordinate,
                filteredListStations(),1000);
        sortStations(nearbyStations,coordinate);
        return nearbyStations;
    }

    private ArrayList<Line> getNearbyFilteredLines (Coordinate coordinate)
    {
        ArrayList<Line> nearbyLines = new ArrayList<>();
        ArrayList<Station> stations = getNearbyFilteredStations(coordinate);
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
