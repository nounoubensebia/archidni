package com.archidni.archidni.Ui.Parking;

import com.archidni.archidni.Data.Places.PlacesRepository;
import com.archidni.archidni.Data.Places.PlacesRepositoryImpl;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;

public class ParkingPresenter  implements ParkingContract.Presenter{

    Parking parking;
    ParkingContract.View view;
    PathPlace userPlace;
    PlacesRepository placesRepository;

    public ParkingPresenter(Parking parking, ParkingContract.View view) {
        this.parking = parking;
        this.view = view;
        placesRepository = new PlacesRepositoryImpl();
    }

    @Override
    public void onCreate() {
        view.showParkingOnActivity(parking);
        getNearbyPlaces();
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
            userPlace = new PathPlace("Ma Positon",coordinate);
        }
    }

    @Override
    public void onPlaceClicked(MainActivityPlace mainActivityPlace) {
        view.startPlaceActivity(mainActivityPlace);
    }

    @Override
    public void onRetryClick() {
        view.hideErrorLayout();
        getNearbyPlaces();
    }

    private void getNearbyPlaces ()
    {
        view.showProgressLayout();
        placesRepository.getNearbyPlaces(parking, new PlacesRepository.OnNearbyPlacesFound() {
            @Override
            public void onNearbyPlacesFound(ArrayList<MainActivityPlace> places) {
                view.hideErrorLayout();
                view.hideProgressLayout();
                view.showNearbyPlacesLayout();
                view.showNearbyPlacesOnList(places,parking);
                if (places.size()==0)
                {
                    view.hideNearbyPlacesLayout();
                }
            }

            @Override
            public void onError() {
                view.hideProgressLayout();
                view.showNearbyPlacesLayout();
                view.showErrorLayout();
            }
        });
    }
}
