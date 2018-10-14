package com.archidni.archidni.Ui.Parking;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;
import java.util.List;

public interface ParkingContract {
    interface View {
        void showParkingOnActivity (Parking parking);
        void startPathSearchActivity (PathPlace origin, PathPlace destination);
        void showParkingOnMap (Parking parking);
        void showNearbyPlacesOnList(ArrayList<MainActivityPlace> places,Parking parking);
        void startPlaceActivity(MainActivityPlace mainActivityPlace);
        void showNearbyPlacesLayout();
        void hideNearbyPlacesLayout();
        void showErrorLayout();
        void hideErrorLayout();
        void showProgressLayout();
        void hideProgressLayout();
    }

    interface Presenter {
        void onCreate();
        void onMapReady();
        void onGetPathClicked();
        void onUserLocationCaptured(Coordinate coordinate);
        void onPlaceClicked(MainActivityPlace mainActivityPlace);
        void onRetryClick();
    }
}
