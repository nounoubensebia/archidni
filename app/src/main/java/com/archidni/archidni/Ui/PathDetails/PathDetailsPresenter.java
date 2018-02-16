package com.archidni.archidni.Ui.PathDetails;

import com.archidni.archidni.Model.Path.Path;

/**
 * Created by noure on 12/02/2018.
 */

public class PathDetailsPresenter implements PathDetailsContract.Presenter {

    private Path path;
    private PathDetailsContract.View view;

    public PathDetailsPresenter(Path path, PathDetailsContract.View view) {
        this.path = path;
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.showPathOnActivity(path);
    }

    @Override
    public void onMapReady() {
        view.showPathOnMap(path);
    }

    @Override
    public void onStartNavigationClick() {
        view.startPathNavigationActivity(path);
    }
}
