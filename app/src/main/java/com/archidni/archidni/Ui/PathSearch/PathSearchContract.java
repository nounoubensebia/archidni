package com.archidni.archidni.Ui.PathSearch;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathPreferences;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by noure on 03/02/2018.
 */

public interface PathSearchContract {
    interface View {
        void showOriginAndDestinationLabels(String origin, String destination);
        void showOriginAndDestinationOnMap(PathPlace origin, PathPlace destination);
        void showLoadingBar();
        void showPathSuggestions (ArrayList<Path> paths, PathSettings pathSettings);
        void startSearchActivity (int requestType);
        void showSetTimeDialog(Calendar departureTime);
        void showSetDateDialog(Calendar departureTime);
        void updateTime(Calendar departureTime);
        void updateDate(Calendar departureTime);
        void hidePathsLayout();
        void showErrorMessage();
        void showOriginNotSet();
        void moveCameraToCoordinate (Coordinate coordinate);
        void startPathDetailsActivity(Path path);
        void hideMapLoadingLayout();
        void showOptionsDialog(PathPreferences pathPreferences);
    }

    interface Presenter {
        void onMapReady();
        void onMapLoaded();
        void onSearchPathsClick(Context context,boolean isArrival);
        void lookForLocation (int requestType);
        void onActivityResult (int requestType,PathPlace newPlace);
        void onDepartureTimeClick();
        void onDepartureDateClick();
        void updateTime(Calendar departureTime);
        void updateDate(Calendar departureTime);
        void onPathItemClick (Path path);
        void onStop(Context context);
        void onOptionsLayoutClicked();
        void onOptionsUpdated(PathPreferences pathPreferences);
    }
}
