package com.archidni.archidni.Ui.Station;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public interface StationContract {
    public interface View {
        void setTheme(Station station);
        void showLinesLoadingBar();
        void showLinesOnList(ArrayList<Line> lines);
        void showTripsOnList(Station station,ArrayList<Line> lines,long departureTime,long departureDate);
        void showNearbyPlacesOnList (Station station, ArrayList<MainActivityPlace> mainActivityPlaces);
        void showTimeDialog(long selectedTime);
        void showDateDialog (long selectedDate);
        void updateTime(long newTime);
        void updateDate(long newDate);
        void startPathSearchActivity (PathPlace origin, PathPlace destination);
        void showStationOnActivity(Station station);
        void showStationOnMap (Station station);
        void updateLinesTripsLayout(int selectedItem,Station station);
        void startLineActivity(Line line);
        void hideTimeText();
        void startStationActivity (Station station);
        void startTrainTripActivity(TrainSchedule trainSchedule);
    }

    public interface Presenter {
        void onGetPathClicked ();
        void onCreate(Context context);
        void onMapReady();
        void onUserLocationCaptured(Coordinate coordinate);
        void toggleSelectedItem(int selectedItem);
        void onLineItemClick(Line line);
        void updateTime(long newTime);
        void updateDate(long newDate);
        void onTimeUpdateClick();
        void onDateUpdateClick();
        void onStop(Context context);
        void onPlaceClick (MainActivityPlace place);
        void onTrainScheduleClick(TrainSchedule trainSchedule);
    }
}
