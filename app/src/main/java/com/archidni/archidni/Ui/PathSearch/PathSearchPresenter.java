package com.archidni.archidni.Ui.PathSearch;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Path.PathSearcher;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSearchPresenter implements PathSearchContract.Presenter {

    private PathSearchContract.View view;
    private PathSearcher pathSearcher;

    public PathSearchPresenter(PathSearchContract.View view, Place origin, Place destination) {
        this.view = view;
        pathSearcher = new PathSearcher(origin,destination,
                TimeUtils.getSecondsFromMidnight(),TimeUtils.getCurrentTimeInSeconds());
        String originString = (origin!=null) ? origin.getMainText():"";
        String destinationString = (destination!=null) ? destination.getMainText():"";
        view.showOriginAndDestination(originString,destinationString);
    }

    private void updateOriginDestination ()
    {
        String originString = (pathSearcher.getOrigin()!=null) ? pathSearcher.getOrigin().getMainText():"";
        String destinationString = (pathSearcher.getDestination().getMainText()!=null) ?
                pathSearcher.getDestination().getMainText():"";
        view.showOriginAndDestination(originString,destinationString);
    }

    @Override
    public void loadPathSuggestions() {
        view.showLoadingBar();
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.showPathSuggestions(new ArrayList<Path>());
            }
        },2000);

    }

    @Override
    public void lookForLocation(int requestType) {
        view.startSearchActivity(requestType);
    }

    @Override
    public void onActivityResult(int requestType, Place newPlace) {
        if (requestType == IntentUtils.SearchIntents.TYPE_LOOK_FOR_OR)
        {
            pathSearcher.setOrigin(newPlace);
        }
        else
        {
            pathSearcher.setDestination(newPlace);
        }
        updateOriginDestination();
    }

    @Override
    public void onDepartureTimeClick() {
        view.showSetTimeDialog(pathSearcher.getDepartureTimeInSeconds());
    }

    @Override
    public void onDepartureDateClick() {
        view.showSetDateDialog(pathSearcher.getDepartureDateInSeconds());
    }

    @Override
    public void updateTime(long departureTime) {
        view.updateTime(departureTime);
    }

    @Override
    public void updateDate(long departureDate) {
        view.updateDate(departureDate);
    }
}
