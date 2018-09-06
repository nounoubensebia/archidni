package com.archidni.archidni.Ui.PathSearch;

import android.content.Context;

import com.archidni.archidni.Data.Paths.PathRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Path.PathPreferences;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSearchPresenter implements PathSearchContract.Presenter {

    private PathSearchContract.View view;
    private PathSettings pathSettings;
    private PathRepository pathRepository;
    private List<Path> paths;

    public PathSearchPresenter(PathSearchContract.View view, PathPlace origin, PathPlace destination) {
        this.view = view;
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(TimeUtils.getCurrentTimeInSeconds()*1000);
        pathSettings = new PathSettings(origin,destination,
                dateCalendar,false,
                PathPreferences.DEFAULT);
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
    public void onMapLoaded() {
        view.hideMapLoadingLayout();
    }

    @Override
    public void onSearchPathsClick(Context context,boolean arrival) {
        if (pathSettings.getOrigin()!=null&&pathSettings.getDestination()!=null) {
            pathSettings.setArriveBy(arrival);
            view.showLoadingBar();
            pathRepository.getPaths(context, pathSettings, new PathRepository.OnSearchCompleted() {
                @Override
                public void onResultsFound(List<Path> foundPaths) {
                    paths = foundPaths;
                    ArrayList<Path> pathsToShow = getSortedAndFilteredPaths();
                    view.showPathSuggestions(pathsToShow,pathSettings);
                }

                @Override
                public void onError() {
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

    private ArrayList<Path> getSortedAndFilteredPaths()
    {
        Collections.sort(paths, new Comparator<Path>() {
            @Override
            public int compare(Path path, Path t1) {
                PathPreferences pathPreferences = pathSettings.getPathPreferences();

                switch (pathPreferences.getSortPreference())
                {
                    case PathPreferences.SORT_BY_MINIMUM_TIME :
                        return (int) (path.getDuration()-t1.getDuration());
                    case PathPreferences.SORT_BY_MINIMUM_WALKING_TIME :
                        return (int) (path.getWalkingTime()-t1.getWalkingTime());
                    case PathPreferences.SORT_BY_MINIMUM_TRANSFERS :
                        return (path.getTransferNumber()-t1.getTransferNumber());
                }
                return -1;
            }
        });
        ArrayList<Path> sortedPaths = new ArrayList<>();
        for (Path path:paths)
        {
            boolean found = false;
            ArrayList<TransportMean> blackListedTransports = pathSettings.getPathPreferences()
                    .getTransportModesNotUsed();
            ArrayList<TransportMean> pathTransportMeans = path.getTransportMeans();
            for (TransportMean transportMean:blackListedTransports)
            {
                if (pathTransportMeans.contains(transportMean))
                {
                    found = true;
                }
            }
            if (!found)
            {
                sortedPaths.add(path);
            }
        }
        return sortedPaths;
    }

    @Override
    public void onDepartureTimeClick() {
        view.showSetTimeDialog(pathSettings.getDepartureArrivalTime());
    }

    @Override
    public void onDepartureDateClick() {
        view.showSetDateDialog(pathSettings.getDepartureArrivalTime());
    }

    @Override
    public void updateTime(Calendar departureTime) {
        view.updateTime(departureTime);
        if (!departureTime.equals(pathSettings.getDepartureArrivalTime()))
        {
            view.hidePathsLayout();
        }
        pathSettings.setDepartureArrivalTime(departureTime);
    }

    @Override
    public void updateDate(Calendar departureTime) {
        view.updateDate(departureTime);
        if (!departureTime.equals(pathSettings.getDepartureArrivalTime()))
        {
            view.hidePathsLayout();
        }
        pathSettings.setDepartureArrivalTime(departureTime);
    }

    @Override
    public void onPathItemClick(Path path) {
        view.startPathDetailsActivity(path);
    }

    @Override
    public void onStop(Context context) {
        pathRepository.cancelRequests(context);
    }

    @Override
    public void onOptionsLayoutClicked() {
        view.showOptionsDialog(pathSettings.getPathPreferences());
    }

    @Override
    public void onOptionsUpdated(PathPreferences pathPreferences) {
        this.pathSettings.setPathPreferences(pathPreferences);
        if (paths!=null)
            view.showPathSuggestions(getSortedAndFilteredPaths(),pathSettings);
    }
}
