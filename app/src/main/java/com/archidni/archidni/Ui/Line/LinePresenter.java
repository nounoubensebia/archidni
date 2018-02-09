package com.archidni.archidni.Ui.Line;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 09/02/2018.
 */

public class LinePresenter implements LineContract.Presenter {
    private Line line;
    private LineContract.View view;
    private Station selectedStation;

    public LinePresenter(Line line, LineContract.View view) {
        this.line = line;
        this.view = view;
        view.setTheme(line);
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
}
