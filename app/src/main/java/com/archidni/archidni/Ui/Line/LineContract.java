package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public interface LineContract {
    public interface View {
        void showStationsOnMap(ArrayList<Station> stations);
        void showLineOnActivity(Line line);
        void setTheme(Line line);
        void showSelectedStation (Station station);
        void deselectStation (ArrayList<Station> stations);
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
    }
}
