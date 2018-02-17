package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Favorites.FavoritesRepository;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 09/02/2018.
 */

public class LinePresenter implements LineContract.Presenter {
    private Line line;
    private LineContract.View view;
    private Station selectedStation;
    private FavoritesRepository favoritesRepository;

    public LinePresenter(Line line, LineContract.View view) {
        this.line = line;
        this.view = view;
        view.setTheme(line);
        favoritesRepository = new FavoritesRepository();
    }

    @Override
    public void onStationClicked(Station station) {
        if (selectedStation!=null&&selectedStation.getId()==station.getId())
        {
            view.deselectStation(line);
            selectedStation = null;
        }
        else
        {
            if (selectedStation==null||selectedStation.getId()!=station.getId())
            {
                view.showSelectedStation(station);
                selectedStation = station;
            }
        }
    }

    @Override
    public void onCreate() {
        view.showLineOnActivity(line);
        if (favoritesRepository.lineExists(App.getAppContext(),line))
        {
            view.showDeleteLineFromFavoritesText();
        }
    }

    @Override
    public void onMapReady() {
        view.showLineOnMap(line);
    }

    @Override
    public void onStationDetailsClicked() {
        view.startStationActivity(selectedStation);
    }

    @Override
    public void loadMenu() {
        if (selectedStation==null)
            view.inflateTripMenu();
        else
            view.inflateStationMenu();
    }

    @Override
    public void onAddDeleteFromFavoritesClicked() {
        if (favoritesRepository.lineExists(App.getAppContext(),line))
        {
            favoritesRepository.deleteLine(App.getAppContext(),line);
            view.showDeletedFromFavoritesMessage();
            view.showAddToFavoritesText();
        }
        else
        {
            favoritesRepository.addLineToFavorites(App.getAppContext(),line);
            view.showAddedToFavoritesMessage();
            view.showDeleteLineFromFavoritesText();
        }
    }

    @Override
    public void onSignalDisturbanceClicked() {
        view.showFeatureNotYetAvailableMessage();
    }
}
