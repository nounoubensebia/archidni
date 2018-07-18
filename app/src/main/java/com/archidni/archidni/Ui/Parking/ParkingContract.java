package com.archidni.archidni.Ui.Parking;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;

public interface ParkingContract {
    interface View {
        void showParkingOnActivity (Parking parking);
        void startPathSearchActivity (PathPlace origin, PathPlace destination);
        void showParkingOnMap (Parking parking);
    }

    interface Presenter {
        void onCreate();
        void onMapReady();
        void onGetPathClicked();
        void onUserLocationCaptured(Coordinate coordinate);
    }
}
