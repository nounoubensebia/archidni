package com.archidni.archidni.Ui.Station;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 09/02/2018.
 */

public class StationPresenter implements StationContract.Presenter {
    private Station station;
    private StationContract.View view;
    private Place userPlace;
    private boolean linesSelected = true;

    public StationPresenter(Station station, StationContract.View view) {
        this.station = station;
        this.view = view;
        view.setTheme(station);
    }

    @Override
    public void onGetPathClicked() {
        view.startPathSearchActivity(userPlace,station);
    }

    @Override
    public void onCreate() {
        view.showStationOnActivity(station);
        view.showLinesLoadingBar();
        view.updateLinesTripsLayout(linesSelected,station);
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
}
