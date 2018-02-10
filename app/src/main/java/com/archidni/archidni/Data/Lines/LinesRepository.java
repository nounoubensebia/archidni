package com.archidni.archidni.Data.Lines;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class LinesRepository {

    private LinesOnlineDataStore linesOnlineDataStore;

    public void getLines(Context context,Coordinate coordinate, final OnSearchCompleted onSearchCompleted)
    {
        LinesOnlineDataStore linesOnlineDataStore = getLinesOnlineDataStoreInstance();
        linesOnlineDataStore.getLines(context,coordinate, new LinesOnlineDataStore.OnSearchCompleted() {
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

    public void getLinesPassingByStation (Context context, Station station, final OnSearchCompleted onSearchCompleted)
    {
        LinesOnlineDataStore linesOnlineDataStore = getLinesOnlineDataStoreInstance();
        linesOnlineDataStore.getLinesPassingByStation(context,station, new LinesOnlineDataStore.OnSearchCompleted() {
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

    private LinesOnlineDataStore getLinesOnlineDataStoreInstance ()
    {
        if (linesOnlineDataStore==null) return new LinesOnlineDataStore();
        else
            return linesOnlineDataStore;

    }
}
