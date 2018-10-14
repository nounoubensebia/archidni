package com.archidni.archidni.Data.Places;

import com.archidni.archidni.Model.Places.MainActivityPlace;

import java.util.ArrayList;

public interface PlacesRepository {


    public void getNearbyPlaces(MainActivityPlace place,OnNearbyPlacesFound onNearbyPlacesFound);

    public interface OnNearbyPlacesFound
    {
        public void onNearbyPlacesFound(ArrayList<MainActivityPlace> places);
        public void onError();
    }
}
