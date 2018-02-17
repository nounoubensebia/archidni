package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 09/02/2018.
 */

public interface LineContract {
    public interface View {
        void showLineOnMap(Line line);
        void showLineOnActivity(Line line);
        void setTheme(Line line);
        void showSelectedStation (Station station);
        void deselectStation (Line line);
        void startStationActivity(Station station);
        void inflateTripMenu();
        void inflateStationMenu();
        void showFeatureNotYetAvailableMessage();
        void showDeleteLineFromFavoritesText();
        void showAddedToFavoritesMessage();
        void showDeletedFromFavoritesMessage();
        void showAddToFavoritesText();
    }

    public interface Presenter {
        void onStationClicked(Station station);
        void onCreate();
        void onMapReady();
        void onStationDetailsClicked();
        void loadMenu();
        void onAddDeleteFromFavoritesClicked();
        void onSignalDisturbanceClicked();
    }
}
