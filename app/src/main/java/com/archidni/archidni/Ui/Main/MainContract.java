package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Places.PathPlace;


import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.UiUtils.ArchidniClusterItem;
import com.archidni.archidni.UiUtils.TransportMeansSelector;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public interface MainContract {
    interface View {
        void updateMeansSelectionLayout(TransportMeansSelector transportMeansSelector);
        void updateStationsLinesLayout(int selectedItem);
        void startSearchActivity(PathPlace userLocation);
        void setUserLocationEnabled(boolean enable);
        void animateCameraToLocation(Coordinate coordinate);
        void moveCameraToLocation (Coordinate coordinate);
        void showSlidingPanel();
        void showLocationLayout(MainActivityPlace place,
                                com.google.android.gms.maps.model.Marker oldSelectedMarker,
                                MainActivityPlace oldSelectedLocation,
                                com.google.android.gms.maps.model.Marker marker,Coordinate userLocation);
        void hideLocationLayout(com.google.android.gms.maps.model.Marker marker, ArchidniClusterItem archidniClusterItem);
        void startPathSearchActivity(PathPlace origin, PathPlace destination);
        void showLinesLoadingLayout();
        void hideLinesLoadingLayout();
        void startParkingActivity(Parking parking);
        void showPlacesOnMap(ArrayList<? extends MainActivityPlace> places,TransportMeansSelector transportMeansSelector);
        void updatePlacesOnMap (ArrayList<? extends MainActivityPlace> places,TransportMeansSelector newTransportMeansSelector,
                                TransportMeansSelector oldTransportMeanSelector);
        void showLinesOnList(ArrayList<Line> lines);
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
        void showPlacesOnList(ArrayList<? extends MainActivityPlace> places, Coordinate userCoordinate);
        void showDisconnectDialog();
        void showDisconnectProgressDialog();
        void hideDisconnectProgressDialog();
        void disconnectUser();
        void showDisconnectionError();
        Coordinate getMapCenter ();
    }

    interface Presenter {
        void toggleTransportMean(int transportMeanId);
        void toggleStationsLines(int selectedItem);
        void onSearchClicked();
        void onMapReady(Context context, LatLngBounds latLngBounds, Coordinate coordinate);
        void onMyLocationFabClick();
        void onUserLocationUpdated(Coordinate userLocation);
        void onShowSlidingPanelFabClick();
        void onLocationMarkerCreated(com.google.android.gms.maps.model.Marker marker);
        void onMapShortClick();
        void onSearchPathClick();
        void onCameraMove(Context context, Coordinate coordinate,float zoom,LatLngBounds latLngBounds);
        void onStationMarkerClick(Station station, com.google.android.gms.maps.model.Marker marker,
                                  ArchidniClusterItem archidniClusterItem);
        void onParkingMarkerClick (Parking parking, com.google.android.gms.maps.model.Marker marker,
                                   ArchidniClusterItem archidniClusterItem);
        void onRetryClicked(Context context,Coordinate coordinate);
        void onStationItemClick(Station station);
        void onLineItemClicked(Line line);
        void onLinesStationsFindClick();
        void onLogoutDialogClick();
        void onLogoutClick();
        void onPlaceClick();
        void onFavoritesClick();
        void onStop(Context context);
        void onParkingClick (Parking parking);
        void onFirstLocationCaptured (Coordinate coordinate);
        void onMapLoaded (Coordinate coordinate,LatLngBounds latLngBounds,double zoom);
        void onDisconnectClick();
    }

    interface OnUserLocationObtainedCallback {
        void onLocationObtained (Coordinate userLocation);
    }
}
