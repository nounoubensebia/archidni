package com.archidni.archidni.Ui.Main;

import android.content.Context;

import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
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
        void updateStationsLinesLayout(boolean stationsSelected);
        void startSearchActivity(Place userLocation);
        void setUserLocationEnabled(boolean enable);
        void moveCameraToUserLocation();
        void animateCameraToLocation(Coordinate coordinate);
        void obtainUserLocation(OnUserLocationObtainedCallback onUserLocationObtainedCallback);
        void trackUser();
        void showSlidingPanel();
        void showLocationLayout(Place place,Place oldPlace);
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
        void startStationActivity(Station station);
        void startLineActivity(Line line);
        void startLinesStationsActivity();
        void showDrawerLayout(User user);
        void logoutUser();
        void hideOverlayLayout();
        void showOverlayLayout();
        void startFavoritesActivity();
    }

    interface Presenter {
        void toggleTransportMean(int transportMeanId);
        void toggleStationsLines(boolean stationsTabbed);
        void onSearchClicked();
        void onMapReady(Context context,BoundingBox boundingBox);
        void onMyLocationFabClick();
        void onShowSlidingPanelFabClick();
        void onLocationMarkerCreated(ArchidniMarker marker);
        void onMapShortClick();
        void onSearchPathClick();
        void onCameraMove(Context context, Coordinate coordinate);
        void onStationMarkerClick(Station station, ArchidniMarker marker);
        void onRetryClicked(Context context,Coordinate coordinate);
        void onStationFabClick();
        void onStationItemClick(Station station);
        void onLineItemClicked(Line line);
        void onLinesStationsFindClick();
        void onLogoutClick();
        void onFavoritesClick();
        void onStop(Context context);
    }

    interface OnUserLocationObtainedCallback {
        void onLocationObtained (Coordinate userLocation);
    }
}
