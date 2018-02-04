package com.archidni.archidni.Ui.PathSearch;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Place;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public interface PathSearchContract {
    interface View {
        void showOriginAndDestination (String origin,String destination);
        void showLoadingBar();
        void showPathSuggestions (ArrayList<Path> paths);
        void startSearchActivity (int requestType);
        void showSetTimeDialog(long departureTime);
        void showSetDateDialog(long departureDate);
        void updateTime(long departureTime);
        void updateDate(long departureDate);
    }

    interface Presenter {
        void loadPathSuggestions ();
        void lookForLocation (int requestType);
        void onActivityResult (int requestType,Place newPlace);
        void onDepartureTimeClick();
        void onDepartureDateClick();
        void updateTime(long departureTime);
        void updateDate(long departureDate);
    }
}
