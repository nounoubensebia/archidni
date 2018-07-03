package com.archidni.archidni.Ui.Parking;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.Parking;

public class ParkingPresenter  implements ParkingContract.Presenter{

    Parking parking;
    ParkingContract.View view;
    Place userPlace;

    public ParkingPresenter(Parking parking, ParkingContract.View view) {
        this.parking = parking;
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.showParkingOnActivity(parking);
    }

    @Override
    public void onMapReady() {
        view.showParkingOnMap(parking);
    }

    @Override
    public void onGetPathClicked() {
        view.startPathSearchActivity(userPlace,parking);
    }

    @Override
    public void onUserLocationCaptured(Coordinate coordinate) {
        if (coordinate!=null)
        {
            userPlace = new Place("Ma Positon","Ma position",coordinate);
        }
    }
}
