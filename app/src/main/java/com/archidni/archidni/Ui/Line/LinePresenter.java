package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Favorites.FavoritesRepository;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Reports.DisruptionSubject;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class LinePresenter implements LineContract.Presenter {
    private Line line;
    private LineContract.View view;
    private Station selectedStation;
    private FavoritesRepository favoritesRepository;
    private boolean outboundSelected;

    public LinePresenter(Line line, LineContract.View view) {
        this.line = line;
        this.view = view;
        view.setTheme(line);
        favoritesRepository = new FavoritesRepository();
        outboundSelected = true;
    }

    @Override
    public void onStationClicked(Station station) {
        if (selectedStation!=null&&selectedStation.getId()==station.getId())
        {
            view.deselectStation(getStations(),getPolyline());
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
        if (line.isBusLine())
        {
            view.showStationsOnList(getStations(),true);
            view.showInboundOutboundLayout();
        }
        else
        {
            view.showStationsOnList(line.getStations(),true);
        }
        if (favoritesRepository.lineExists(App.getAppContext(),line))
        {
            view.showDeleteLineFromFavoritesText();
        }
    }

    @Override
    public void onMapReady()
    {
        view.moveCamera(GeoUtils.getPolylineCenter(getPolyline()),12);
        view.showLineOnMap(getPolyline(),getStations());
    }

    private ArrayList<Coordinate> getPolyline ()
    {
        if (line.isBusLine())
        {
            return line.getPolyline(outboundSelected);
        }
        else
            return line.getPolyline();
    }

    private ArrayList<Station> getStations ()
    {
        if (line.isBusLine())
        {
            if (outboundSelected)
            {
                return line.getOutboundStations();
            }
            else
            {
                return line.getInboundStations();
            }
        }
        else
        {
            return line.getStations();
        }
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
        view.startDisruptionsActivity(new DisruptionSubject(line));
    }

    @Override
    public void onInboundOutboundClicked(boolean outboundClicked) {
        outboundSelected = outboundClicked;
        view.updateInboundOutboundLayout(outboundSelected);
        view.showStationsOnList(getStations(),false);
        view.showLineOnMap(getPolyline(),getStations());
    }

    @Override
    public void onMapLoaded() {
        view.hideMapLoadingLayout();
    }

    @Override
    public void onShowNewsAndNotificationsClick() {
        view.startNewsAndNotificationsActivity(line);
    }

    @Override
    public void onTarifsClicked() {
        view.startTarifsActivity(line);
    }

    @Override
    public void onScheduleClicked() {
        view.startSchedulesActivity(line);
    }
}
