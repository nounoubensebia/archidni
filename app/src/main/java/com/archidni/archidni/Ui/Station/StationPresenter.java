package com.archidni.archidni.Ui.Station;

import android.content.Context;

import com.archidni.archidni.Data.Lines.LinesRepository;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

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
        if (linesSelected!=this.linesSelected)
        {
            this.linesSelected = linesSelected;
        }
        view.updateLinesTripsLayout(this.linesSelected,station);
    }

    @Override
    public void onLineItemClick(Line line) {
        view.startLineActivity(line);
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
