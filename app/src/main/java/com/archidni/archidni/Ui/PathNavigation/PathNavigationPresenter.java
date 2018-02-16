package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.Model.Path.Path;

/**
 * Created by nouno on 16/02/2018.
 */

public class PathNavigationPresenter implements PathNavigationContract.Presenter {
    private PathNavigationContract.View view;
    private Path path;
    private int selectedStep;

    public PathNavigationPresenter(PathNavigationContract.View view, Path path) {
        this.view = view;
        this.path = path;
        selectedStep = 0;
    }

    @Override
    public void goToNextStep() {
        if (selectedStep+1<path.getPathSteps().size())
        {
            selectedStep++;
            view.showStepOnActivity(path.getPathSteps().get(selectedStep),selectedStep+1);
            view.showStepOnMap(path.getPathSteps().get(selectedStep),path);
        }
    }

    @Override
    public void goToPreviousStep() {
        if (selectedStep-1>=0)
        {
            selectedStep--;
            view.showStepOnActivity(path.getPathSteps().get(selectedStep),selectedStep+1);
            view.showStepOnMap(path.getPathSteps().get(selectedStep),path);
        }

    }

    @Override
    public void onMapReady() {
        view.showPathOnMap(path);
        view.showStepOnMap(path.getPathSteps().get(selectedStep),path);
    }

    @Override
    public void onCreate() {
        view.showStepOnActivity(path.getPathSteps().get(selectedStep),selectedStep+1);
    }
}
