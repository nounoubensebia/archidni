package com.archidni.archidni.Ui.Main;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

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
        void moveCameraToLocation(Coordinate coordinate);
        void obtainUserLocation(OnUserLocationObtainedCallback onUserLocationObtainedCallback);
        void trackUser();
        void showSlidingPanel();
        void showLocationLayout(Place place);
        void hideLocationLayout();
    }

    interface Presenter {
        void toggleTransportMean(int transportMeanId);
        void toggleStationsLines(boolean stationsTabbed);
        void onSearchClicked();
        void onMapReady();
        void onMyLocationFabClick();
        void onShowSlidingPanelFabClick();
        void onMapLongClick(Coordinate coordinate);
        void onMapShortClick();
    }

    interface OnUserLocationObtainedCallback {
        void onLocationObtained (Coordinate userLocation);
    }
}
