package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.MapPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.UiUtils.ArchidniMarker;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public interface MainContract {
    interface View {
        void updateMeansSelectionLayout(TransportMeansSelector transportMeansSelector);
        void updateStationsLinesLayout(int selectedItem);
        void startSearchActivity(Place userLocation);
        void setUserLocationEnabled(boolean enable);
        void moveCameraToUserLocation();
        void animateCameraToLocation(Coordinate coordinate);
        void obtainUserLocation(OnUserLocationObtainedCallback onUserLocationObtainedCallback);
        void trackUser();
        void showSlidingPanel();
        void showLocationLayout(MapPlace place, com.google.android.gms.maps.model.Marker oldSelectedMarker, com.google.android.gms.maps.model.Marker marker);
        void hideLocationLayout(com.google.android.gms.maps.model.Marker marker);
        void startPathSearchActivity(Place origin,Place destination);
        void showLinesLoadingLayout();
        void hideLinesLoadingLayout();
        void startParkingActivity(Parking parking);
        void showPlacesOnMap(ArrayList<? extends Place> places,TransportMeansSelector transportMeansSelector);
        void updatePlacesOnMap (ArrayList<? extends Place> places,TransportMeansSelector transportMeansSelector);
        void showLinesOnList(ArrayList<Line> lines);
        void showStationsOnList(ArrayList<Station> stations,Coordinate userCoordinate);
        void showZoomInsufficientLayout();
        void hideZoomInsufficientLayout();
        void showSearchErrorLayout();
        void hideSearchErrorLayout();
        void startStationActivity(Station station);
        void startLineActivity(Line line);
        void startLinesStationsActivity();
        void showDrawerLayout(User user);
        void logoutUser();
        void hideOverlayLayout();
        void showOverlayLayout();
        void startFavoritesActivity();
        void showPlacesOnList(ArrayList<Parking> places);
        Coordinate getMapCenter ();
    }

    interface Presenter {
        void toggleTransportMean(int transportMeanId);
        void toggleStationsLines(int selectedItem);
        void onSearchClicked();
        void onMapReady(Context context,BoundingBox boundingBox,Coordinate coordinate);
        void onMyLocationFabClick();
        void onShowSlidingPanelFabClick();
        void onLocationMarkerCreated(com.google.android.gms.maps.model.Marker marker);
        void onMapShortClick();
        void onSearchPathClick();
        void onCameraMove(Context context, Coordinate coordinate);
        void onStationMarkerClick(Station station, com.google.android.gms.maps.model.Marker marker);
        void onParkingMarkerClick (Parking parking, com.google.android.gms.maps.model.Marker marker);
        void onRetryClicked(Context context,Coordinate coordinate);
        void onStationItemClick(Station station);
        void onLineItemClicked(Line line);
        void onLinesStationsFindClick();
        void onLogoutClick();
        void onPlaceClick();
        void onFavoritesClick();
        void onStop(Context context);
        void onParkingClick (Parking parking);
    }

    interface OnUserLocationObtainedCallback {
        void onLocationObtained (Coordinate userLocation);
    }
}
