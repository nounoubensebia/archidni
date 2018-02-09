package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
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
        void updateStationsLinesLayout(boolean stationsSelected);
        void startSearchActivity(Place userLocation);
        void setUserLocationEnabled(boolean enable);
        void moveCameraToUserLocation();
        void animateCameraToLocation(Coordinate coordinate);
        void obtainUserLocation(OnUserLocationObtainedCallback onUserLocationObtainedCallback);
        void trackUser();
        void showSlidingPanel();
        void showLocationLayout(Place place);
        void hideLocationLayout(ArchidniMarker archidniMarker);
        void startPathSearchActivity(Place origin,Place destination);
        void showLinesLoadingLayout();
        void hideLinesLoadingLayout();
        void showStationsOnMap(ArrayList<Station> stations);
        void showLinesOnList(ArrayList<Line> lines);
        void showStationsOnList(ArrayList<Station> stations,Coordinate userCoordinate);
        void showZoomInsufficientLayout();
        void hideZoomInsufficientLayout();
        void showSearchErrorLayout();
        void hideSearchErrorLayout();
    }

    interface Presenter {
        void toggleTransportMean(int transportMeanId);
        void toggleStationsLines(boolean stationsTabbed);
        void onSearchClicked();
        void onMapReady(Context context,BoundingBox boundingBox);
        void onMyLocationFabClick();
        void onShowSlidingPanelFabClick();
        void onMapLongClick(Coordinate coordinate);
        void onLocationMarkerCreated(ArchidniMarker marker);
        void onMapShortClick();
        void onSearchPathClick();
        void onCameraMove(Context context, Coordinate coordinate, double zoom, BoundingBox boundingBox);
        void onStationMarkerClick(Station station, ArchidniMarker marker);
        void onRetryClicked(Context context,Coordinate coordinate);
    }

    interface OnUserLocationObtainedCallback {
        void onLocationObtained (Coordinate userLocation);
    }
}
