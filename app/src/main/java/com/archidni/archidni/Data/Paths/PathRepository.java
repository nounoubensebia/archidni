package com.archidni.archidni.Data.Paths;

import android.content.Context;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;

import java.util.ArrayList;

/**
 * Created by noure on 12/02/2018.
 */

public class PathRepository {
    public void getPaths (Context context,PathSettings pathSettings, final OnSearchCompleted onSearchCompleted)
    {
        new PathOnlineDataStore().getPaths(context,pathSettings, new PathOnlineDataStore.OnSearchCompleted() {
            @Override
            public void onResultsFound(ArrayList<Path> paths) {
                onSearchCompleted.onResultsFound(paths);
            }

            @Override
            public void onError() {
                onSearchCompleted.onError();
            }
        });
    }

    public void cancelRequests (Context context)
    {
        new PathOnlineDataStore().cancelRequests(context);
    }

    public interface OnSearchCompleted {
        void onResultsFound (ArrayList<Path> paths);
        void onError();
    }
}
