package com.archidni.archidni.Ui.PathSearch;

import android.content.Context;

import com.archidni.archidni.Data.Paths.PathRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSearchPresenter implements PathSearchContract.Presenter {

    private PathSearchContract.View view;
    private PathSettings pathSettings;
    private PathRepository pathRepository;

    public PathSearchPresenter(PathSearchContract.View view, PathPlace origin, PathPlace destination) {
        this.view = view;
        pathSettings = new PathSettings(origin,destination,
                TimeUtils.getSecondsFromMidnight(),TimeUtils.getCurrentTimeInSeconds());
        String originString = (origin!=null) ? origin.getTitle():"";
        String destinationString = (destination!=null) ? destination.getTitle():"";
        view.showOriginAndDestinationLabels(originString,destinationString);
        pathRepository = new PathRepository();
    }

    private void updateOriginDestination ()
    {
        String originString = (pathSettings.getOrigin()!=null) ? pathSettings.getOrigin().getTitle():"";
        String destinationString = (pathSettings.getDestination().getTitle()!=null) ?
                pathSettings.getDestination().getTitle():"";
        view.showOriginAndDestinationLabels(originString,destinationString);
        view.showOriginAndDestinationOnMap(pathSettings.getOrigin(), pathSettings.getDestination());
    }

    @Override
    public void onMapReady() {
        view.moveCameraToCoordinate(pathSettings.getDestination().getCoordinate());
        view.showOriginAndDestinationOnMap(pathSettings.getOrigin(), pathSettings.getDestination());
    }

    @Override
    public void onSearchPathsClick(Context context) {
        if (pathSettings.getOrigin()!=null&&pathSettings.getDestination()!=null) {
            view.showLoadingBar();
            pathRepository.getPaths(context, pathSettings, new PathRepository.OnSearchCompleted() {
                @Override
                public void onResultsFound(ArrayList<Path> paths) {
                    view.showPathSuggestions(paths,pathSettings);
                }

                @Override
                public void onError() {
                    view.hidePathsLayout();
                    view.showErrorMessage();
                }
            });
        }
        else
        {
            view.showOriginNotSet();
        }
    }

    @Override
    public void lookForLocation(int requestType) {
        view.startSearchActivity(requestType);
    }

    @Override
    public void onActivityResult(int requestType, PathPlace newPlace) {
        if (requestType == IntentUtils.SearchIntents.TYPE_LOOK_FOR_OR)
        {
            if (pathSettings.getOrigin()==null||!newPlace.getCoordinate().equals(pathSettings.getOrigin().getCoordinate()))
            {
               view.hidePathsLayout();
            }
            pathSettings.setOrigin(newPlace);
        }
        else
        {
            if (!newPlace.getCoordinate().equals(pathSettings.getDestination().getCoordinate()))
            {
                view.hidePathsLayout();
            }
            pathSettings.setDestination(newPlace);
        }
        updateOriginDestination();
    }

    @Override
    public void onDepartureTimeClick() {
        view.showSetTimeDialog(pathSettings.getDepartureTime());
    }

    @Override
    public void onDepartureDateClick() {
        view.showSetDateDialog(pathSettings.getDepartureDate());
    }

    @Override
    public void updateTime(long departureTime) {
        view.updateTime(departureTime);
        if (departureTime!=pathSettings.getDepartureTime())
        {
            view.hidePathsLayout();
        }
        pathSettings.setDepartureTime(departureTime);
    }

    @Override
    public void updateDate(long departureDate) {
        view.updateDate(departureDate);
        if (departureDate!=pathSettings.getDepartureDate())
        {
            view.hidePathsLayout();
        }
        pathSettings.setDepartureDate(departureDate);
    }

    @Override
    public void onPathItemClick(Path path) {
        view.startPathDetailsActivity(path);
    }

    @Override
    public void onStop(Context context) {
        pathRepository.cancelRequests(context);
    }
}
