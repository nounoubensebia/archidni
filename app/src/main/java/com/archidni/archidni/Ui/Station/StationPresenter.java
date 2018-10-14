package com.archidni.archidni.Ui.Station;

import android.content.Context;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.Data.Station.StationDataRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nouno on 09/02/2018.
 */

public class StationPresenter implements StationContract.Presenter {
    private Station station;
    private StationContract.View view;
    private PathPlace userPlace;
    private int selectedItem;
    private ArrayList<Line> lines;
    private ArrayList<MainActivityPlace> nearbyPlaces;
    private LinesAndPlacesRepository linesAndPlacesRepository;
    private StationDataRepository stationDataRepository;
    private long departureTime;
    private long departureDate;

    public static final int LINES = 0;
    public static final int TRIPS = 1;
    public static final int NEARBY_PLACES = 2;

    public StationPresenter(Station station, StationContract.View view) {
        this.station = station;
        this.view = view;
        view.setTheme(station);
        linesAndPlacesRepository = new LinesAndPlacesRepository();
        stationDataRepository = new StationDataRepository();
    }

    @Override
    public void onGetPathClicked() {
        view.startPathSearchActivity(userPlace,station);
    }

    @Override
    public void onCreate(Context context) {
        selectedItem = 0;
        view.showStationOnActivity(station);
        view.showLinesLoadingBar();
        view.updateLinesTripsLayout(selectedItem,station);
        getLines(context);
        departureTime = TimeUtils.getSecondsFromMidnight();
        departureDate = TimeUtils.getCurrentTimeInSeconds();
        view.updateTime(departureTime);
        if (station.getTransportMean().getId()!=1)
        {
            view.hideTimeText();
        }
    }

    @Override
    public void onMapReady() {
        view.showStationOnMap(station);
    }

    @Override
    public void onUserLocationCaptured(Coordinate coordinate) {
        if (coordinate!=null)
        {
            userPlace = new PathPlace("Ma Positon",coordinate);
        }
    }

    @Override
    public void toggleSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        if (this.lines!=null)
        {

            switch (selectedItem)
            {
                case LINES : view.showLinesOnList(lines);
                break;
                case TRIPS : view.showTripsOnList(station,lines,departureTime,departureDate);
                break;
                case NEARBY_PLACES : view.showNearbyPlacesOnList(station, nearbyPlaces);
            }
        }
        view.updateLinesTripsLayout(selectedItem,station);
    }

    @Override
    public void onLineItemClick(Line line) {
        view.startLineActivity(line);
    }

    @Override
    public void updateTime(long newTime) {
        view.updateTime(newTime);
        departureTime = newTime;
        view.showTripsOnList(station,lines,departureTime,departureDate);
    }

    @Override
    public void updateDate(long newDate) {
        view.updateDate(newDate);
        departureDate = newDate;
        view.showTripsOnList(station,lines,departureTime,departureDate);
    }

    @Override
    public void onTimeUpdateClick() {
        view.showTimeDialog(departureTime);
    }

    @Override
    public void onDateUpdateClick() {
        view.showDateDialog(departureDate);
    }

    @Override
    public void onStop(Context context) {
        linesAndPlacesRepository.cancelAllRequests(context);
    }

    @Override
    public void onPlaceClick(MainActivityPlace place) {
        if (place instanceof Station)
        {
            view.startStationActivity((Station) place);
        }
        else
        {
            view.startPlaceActivity(place);
        }
    }

    @Override
    public void onTrainScheduleClick(TrainSchedule trainSchedule) {
        view.startTrainTripActivity(trainSchedule);
    }

    @Override
    public void onRetryClick(Context context) {
        view.hideErrorLayout();
        view.showProgressLayout();
        getLines(context);
    }

    private void getLines(final Context context) {
        linesAndPlacesRepository.getLinesPassingByStation(context, station, new LinesAndPlacesRepository.OnSearchCompleted() {
            @Override
            public void onLinesFound(final ArrayList<Line> lines) {
                /*StationPresenter.this.lines = lines;
                switch (selectedItem)
                {
                    case LINES : view.showLinesOnList(lines);
                        break;
                    case TRIPS : view.showTripsOnList(station,lines,departureTime,departureDate);
                        break;
                    case NEARBY_PLACES : view.showNearbyPlacesOnList(station,nearbyPlaces);
                }*/
                stationDataRepository.getNearbyPlaces(context, station, new StationDataRepository.OnNearbyPlacesSearchComplete() {
                    @Override
                    public void onSearchComplete(ArrayList<MainActivityPlace> mainActivityPlaces) {
                        sortNearbyPlaces(mainActivityPlaces);
                        StationPresenter.this.nearbyPlaces = mainActivityPlaces;
                        StationPresenter.this.lines = lines;
                        switch (selectedItem)
                        {
                            case LINES : view.showLinesOnList(lines);
                                break;
                            case TRIPS : view.showTripsOnList(station,lines,departureTime,departureDate);
                                break;
                            case NEARBY_PLACES : view.showNearbyPlacesOnList(station,nearbyPlaces);
                              view.showNearbyPlacesOnList(station,nearbyPlaces);
                              break;
                        }

                    }

                    @Override
                    public void onError() {
                        view.hideProgressLayout();
                        view.showErrorLayout();
                    }
                });
            }

            @Override
            public void onError() {
                view.hideProgressLayout();
                view.showErrorLayout();
            }
        });
    }

    private void sortNearbyPlaces (List<MainActivityPlace> places)
    {
        Collections.sort(places, new Comparator<MainActivityPlace>() {
            @Override
            public int compare(MainActivityPlace place, MainActivityPlace t1) {
                return (GeoUtils.distance(station.getCoordinate(),place.getCoordinate())-(GeoUtils.distance(
                        station.getCoordinate(),t1.getCoordinate()
                )));
            }
        });
    }
}
