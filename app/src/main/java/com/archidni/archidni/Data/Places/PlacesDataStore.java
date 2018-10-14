package com.archidni.archidni.Data.Places;

import com.archidni.archidni.Model.Places.MainActivityPlace;

public interface PlacesDataStore {
    public void getNearbyPlaces (MainActivityPlace place, PlacesRepository.OnNearbyPlacesFound onNearbyPlacesFound);
}
