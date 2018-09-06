package com.archidni.archidni.Data.Paths;

import android.content.Context;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;

import java.util.ArrayList;
import java.util.List;

public interface PathDataStore {
    public void getPaths (Context context,PathSettings pathSettings, OnSearchCompleted onSearchCompleted);

    public interface OnSearchCompleted {
        void onResultsFound (List<Path> paths);
        void onError();
    }
}
