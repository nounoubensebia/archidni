package com.archidni.archidni.Data.LinesAndPlaces;

import android.content.Context;

import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noure on 07/02/2018.
 */

public class LinesAndPlacesRepository {

    private LinesAndPlacesOnlineDataStore linesAndPlacesOnlineDataStore;



    public void getLine (Context context, LineStationSuggestion lineStationSuggestion,
                         final OnLineSearchCompleted onLineSearchCompleted)
    {
        getLinesOnlineDataStoreInstance().getLine(context, lineStationSuggestion,
                new LinesAndPlacesOnlineDataStore.OnLineSearchCompleted() {
            @Override
            public void onLineFound(Line line) {
                onLineSearchCompleted.onLineFound(line);
            }

            @Override
            public void onError() {
                onLineSearchCompleted.onError();
            }
        });
    }

    public void cancelAllRequests (Context context)
    {
        getLinesOnlineDataStoreInstance().cancelRequests(context);
    }

    public void getLinesAndPlaces(Context context, final OnLinesAndPlacesSearchCompleted onSearchCompleted)
    {
        LinesAndPlacesOnlineDataStore linesAndPlacesOnlineDataStore = getLinesOnlineDataStoreInstance();
        linesAndPlacesOnlineDataStore.getLinesAndPlaces(context,new LinesAndPlacesOnlineDataStore.OnLinesAndPlacesSearchCompleted() {
            @Override
            public void onLinesAndPlacesFound(ArrayList<Line> lines, ArrayList<MainActivityPlace> places) {
                onSearchCompleted.onFound(lines,places);
            }

            @Override
            public void onError() {
                onSearchCompleted.onError();
            }
        });
    }

    public void getLinesPassingByStation (Context context, Station station, final OnSearchCompleted onSearchCompleted)
    {
        LinesAndPlacesOnlineDataStore linesAndPlacesOnlineDataStore = getLinesOnlineDataStoreInstance();
        linesAndPlacesOnlineDataStore.getLinesPassingByStation(context,station, new LinesAndPlacesOnlineDataStore.OnLinesSearchCompleted() {
            @Override
            public void onLinesFound(ArrayList<Line> lines) {
                onSearchCompleted.onLinesFound(lines);
            }

            @Override
            public void onError() {
                onSearchCompleted.onError();
            }
        });
    }

    public void getNotifications (Line line,OnNotificationsFound onNotificationsFound)
    {
        LinesAndPlacesOnlineDataStore linesAndPlacesOnlineDataStore = getLinesOnlineDataStoreInstance();
        linesAndPlacesOnlineDataStore.getNotifications(line,onNotificationsFound);
    }

    public void getSchedules (Line line,OnSchedulesSearchCompleted onSchedulesSearchCompleted)
    {
        getLinesOnlineDataStoreInstance().getSchedules(line,onSchedulesSearchCompleted);
    }

    public interface OnSearchCompleted {
        void onLinesFound(ArrayList<Line> lines);
        void onError();
    }

    public interface OnLinesAndPlacesSearchCompleted
    {
        void onFound (ArrayList<Line> lines, ArrayList<MainActivityPlace> places);
        void onError ();
    }

    private LinesAndPlacesOnlineDataStore getLinesOnlineDataStoreInstance ()
    {
        if (linesAndPlacesOnlineDataStore ==null) return new LinesAndPlacesOnlineDataStore();
        else
            return linesAndPlacesOnlineDataStore;

    }

    public interface OnLineSearchCompleted {
        void onLineFound (Line line);
        void onError();
    }

    public interface OnNotificationsFound {
        void onNotificationsFound (ArrayList<Notification> notifications);
        void onError();
    }

    public interface OnSchedulesSearchCompleted {
        void onSchedulesFound(List<Schedule> schedules);

        void onError();
    }

}
