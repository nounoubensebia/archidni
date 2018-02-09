package com.archidni.archidni.Ui.Station;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.Trip;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public interface StationContract {
    public interface View {
        void setTheme(Station station);
        void showLinesLoadingBar();
        void showLinesOnList(ArrayList<? extends Line> lines);
        void showTripsOnList(ArrayList<? extends Trip> trips);
        void showTimeDialog(long selectedTime);
        void showDateDialog (long selectedDate);
        void updateTime(long newTime);
        void updateDate(long newDate);
        void startPathSearchActivity (Place origin,Place destination);
        void showStationOnActivity(Station station);
        void showStationOnMap (Station station);
        void updateLinesTripsLayout(boolean linesSelected,Station station);
    }

    public interface Presenter {
        void onGetPathClicked ();
        void onCreate();
        void onMapReady();
        void onUserLocationCaptured(Coordinate coordinate);
        void toggleLinesTrips (boolean linesSelected);
    }
}
