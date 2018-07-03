package com.archidni.archidni.Ui.Parking;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.Parking;

public interface ParkingContract {
    interface View {
        void showParkingOnActivity (Parking parking);
        void startPathSearchActivity (Place origin, Place destination);
        void showParkingOnMap (Parking parking);
    }

    interface Presenter {
        void onCreate();
        void onMapReady();
        void onGetPathClicked();
        void onUserLocationCaptured(Coordinate coordinate);
    }
}
