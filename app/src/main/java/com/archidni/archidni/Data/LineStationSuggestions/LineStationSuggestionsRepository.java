package com.archidni.archidni.Data.LineStationSuggestions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

/**
 * Created by nouno on 15/02/2018.
 */

public class LineStationSuggestionsRepository {
    public int requestSent = 0;

    public void cancelRequests (Context context)
    {
        new LineStationDataStore().cancelRequests(context);
    }

    public void getLineSuggestions (Context context, String text, final OnSearchComplete onSearchComplete)
    {
        requestSent++;
        Log.i("TAGO",requestSent+"");
        LineStationDataStore lineStationDataStore = new LineStationDataStore();
        lineStationDataStore.getLineSuggestions(context, text,
                new LineStationDataStore.OnSearchComplete() {
            @Override
            public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                onSearchComplete.onSearchComplete(lineStationSuggestions);
            }

            @Override
            public void onError() {
                onSearchComplete.onError();
            }
        });
    }

    public void getStationSuggestions (Context context, String text, final OnSearchComplete onSearchComplete)
    {
        LineStationDataStore lineStationDataStore = new LineStationDataStore();
        lineStationDataStore.getStationSuggestions(context, text,
                new LineStationDataStore.OnSearchComplete() {
            @Override
            public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                onSearchComplete.onSearchComplete(lineStationSuggestions);
            }

            @Override
            public void onError() {
                onSearchComplete.onError();
            }
        });
    }

    public interface OnSearchComplete {
        void onSearchComplete (ArrayList<LineStationSuggestion> lineStationSuggestions);
        void onError ();
    }
}
