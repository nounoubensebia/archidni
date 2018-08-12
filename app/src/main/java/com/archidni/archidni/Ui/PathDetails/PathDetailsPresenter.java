package com.archidni.archidni.Ui.PathDetails;

import android.content.Context;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.Transport.Station;


/**
 * Created by noure on 12/02/2018.
 */

public class PathDetailsPresenter implements PathDetailsContract.Presenter {

    private Path path;
    private PathDetailsContract.View view;

    private LinesAndPlacesRepository linesAndPlacesRepository;

    public PathDetailsPresenter(Path path, PathDetailsContract.View view) {
        this.path = path;
        this.view = view;
        linesAndPlacesRepository = new LinesAndPlacesRepository();
    }

    @Override
    public void onCreate() {
        view.showPathOnActivity(path);
    }

    @Override
    public void onMapReady() {
        view.showPathOnMap(path);
    }

    /*@Override
    public void onMapPrepared() {

    }*/

    @Override
    public void onStartNavigationClick() {
        view.startPathNavigationActivity(path,0);
    }

    @Override
    public void onLineItemClick(Context context,LineSkeleton lineSkeleton) {
        view.showLineSearchDialog();
        linesAndPlacesRepository.getLine(context, new LineStationSuggestion(lineSkeleton.getName(),
                        lineSkeleton.getId(),lineSkeleton.getTransportMean().getId()),
                new LinesAndPlacesRepository.OnLineSearchCompleted() {
                    @Override
                    public void onLineFound(Line line) {
                        view.hideLineSearchDialog();
                        view.startLineActivity(line);
                    }

                    @Override
                    public void onError() {
                        view.showLineSearchError();
                    }
                });
    }

    @Override
    public void onStationItemClick(Context context, Station station) {
        view.startStationActivity(station);
    }
}
