package com.archidni.archidni.Ui.Station;

import android.content.Context;

import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class StationPresenter implements StationContract.Presenter {
    private Station station;
    private StationContract.View view;
    private Place userPlace;
    private boolean linesSelected = true;
    private ArrayList<Line> lines;
    private LinesRepository linesRepository;
    private long departureTime;
    private long departureDate;

    public StationPresenter(Station station, StationContract.View view) {
        this.station = station;
        this.view = view;
        view.setTheme(station);
        linesRepository = new LinesRepository();
    }

    @Override
    public void onGetPathClicked() {
        view.startPathSearchActivity(userPlace,station);
    }

    @Override
    public void onCreate(Context context) {
        view.showStationOnActivity(station);
        view.showLinesLoadingBar();
        view.updateLinesTripsLayout(linesSelected,station);
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
            userPlace = new Place("Ma Positon","Ma position",coordinate);
        }
    }

    @Override
    public void toggleLinesTrips(boolean linesSelected) {
        if (this.lines!=null)
        {
            if (linesSelected!=this.linesSelected)
            {
                this.linesSelected = linesSelected;
                if (this.linesSelected)
                {
                    view.showLinesOnList(lines);
                }
                else
                {
                    view.showTripsOnList(station,lines,departureTime,departureDate);
                }
            }
            view.updateLinesTripsLayout(this.linesSelected,station);
        }
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

    private void getLines(Context context) {
        linesRepository.getLinesPassingByStation(context, station, new LinesRepository.OnSearchCompleted() {
            @Override
            public void onLinesFound(ArrayList<Line> lines) {
                StationPresenter.this.lines = lines;
                view.showLinesOnList(lines);
            }

            @Override
            public void onError() {

            }
        });
    }
}
