package com.archidni.archidni.Data.Places;

import com.archidni.archidni.Model.Places.MainActivityPlace;

public class PlacesRepositoryImpl implements PlacesRepository {

    @Override
    public void getNearbyPlaces(MainActivityPlace place, OnNearbyPlacesFound onNearbyPlacesFound) {
        PlacesDataStoreImpl placesDataStore = new PlacesDataStoreImpl();
        placesDataStore.getNearbyPlaces(place,onNearbyPlacesFound);
    }
}
