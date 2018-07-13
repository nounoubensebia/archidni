package com.archidni.archidni.Data.LinesAndPlaces;

import android.content.Context;

import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

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
            public void onLinesAndPlacesFound(ArrayList<Line> lines, ArrayList<Place> places) {
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

    public interface OnSearchCompleted {
        void onLinesFound(ArrayList<Line> lines);
        void onError();
    }

    public interface OnLinesAndPlacesSearchCompleted
    {
        void onFound (ArrayList<Line> lines, ArrayList<Place> places);
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

}
