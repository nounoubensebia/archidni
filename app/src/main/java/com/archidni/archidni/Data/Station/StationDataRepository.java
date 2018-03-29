package com.archidni.archidni.Data.Station;

import android.content.Context;

import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 15/02/2018.
 */

public class StationDataRepository {
    public void getStation (Context context, LineStationSuggestion lineStationSuggestion,
                            final OnSearchComplete onSearchComplete)
    {
        StationDataStore stationDataStore = new StationDataStore();
        stationDataStore.getStation(context, lineStationSuggestion, new StationDataStore.OnSearchComplete() {
            @Override
            public void onSearchComplete(Station station) {
                onSearchComplete.onSearchComplete(station);
            }

            @Override
            public void onError() {
                onSearchComplete.onError();
            }
        });
    }

    public void cancelAllRequests (Context context)
    {
        new StationDataStore().cancelRequests(context);
    }

    public interface OnSearchComplete {
        public void onSearchComplete (Station station);
        public void onError ();
    }
}
