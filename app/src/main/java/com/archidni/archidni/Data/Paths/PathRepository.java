package com.archidni.archidni.Data.Paths;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;

import java.util.ArrayList;

/**
 * Created by noure on 12/02/2018.
 */

public class PathRepository {
    public void getPaths (PathSettings pathSettings, final OnSearchCompleted onSearchCompleted)
    {
        new PathOnlineDataStore().getPaths(pathSettings, new PathOnlineDataStore.OnSearchCompleted() {
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

    public interface OnSearchCompleted {
        void onResultsFound (ArrayList<Path> paths);
        void onError();
    }
}
