package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public interface LineContract {
    public interface View {
        void showLineOnMap(ArrayList<Coordinate> polyline, ArrayList<Station> stations);
        void showLineOnActivity(Line line);
        void setTheme(Line line);
        void showSelectedStation (Station station);
        void deselectStation (ArrayList<Station> stations,ArrayList<Coordinate> polyline);
        void startStationActivity(Station station);
        void inflateTripMenu();
        void inflateStationMenu();
        void showFeatureNotYetAvailableMessage();
        void showDeleteLineFromFavoritesText();
        void showAddedToFavoritesMessage();
        void showDeletedFromFavoritesMessage();
        void showAddToFavoritesText();
        void showStationsOnList (ArrayList<Station> stations);
        void updateInboundOutboundLayout (boolean outboundSelected);
        void showInboundOutboundLayout();
        void hideMapLoadingLayout ();
        void moveCamera (Coordinate coordinate,int zoom);
        void startNewsAndNotificationsActivity(Line line);
    }

    public interface Presenter {
        void onStationClicked(Station station);
        void onCreate();
        void onMapReady();
        void onStationDetailsClicked();
        void loadMenu();
        void onAddDeleteFromFavoritesClicked();
        void onSignalDisturbanceClicked();
        void onInboundOutboundClicked(boolean outboundClicked);
        void onMapLoaded ();
        void onShowNewsAndNotificationsClick();
    }
}
