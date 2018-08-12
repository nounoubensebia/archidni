package com.archidni.archidni.Data.Paths;

import android.content.Context;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;

import java.util.ArrayList;

public interface PathDataStore {
    public void getPaths (Context context,PathSettings pathSettings, OnSearchCompleted onSearchCompleted);

    public interface OnSearchCompleted {
        void onResultsFound (ArrayList<Path> paths);
        void onError();
    }
}
