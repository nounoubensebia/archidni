package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.Model.Path.Path;

/**
 * Created by nouno on 16/02/2018.
 */

public class PathNavigationPresenter implements PathNavigationContract.Presenter {
    private PathNavigationContract.View view;
    private Path path;
    private int selectedStep;

    public PathNavigationPresenter(PathNavigationContract.View view, Path path,int selectedStep) {
        this.view = view;
        this.path = path;
        this.selectedStep = selectedStep;
    }

    @Override
    public void goToNextInstruction() {
        if (selectedStep+1<path.getPathInstructions().size())
        {
            selectedStep++;
            view.showInstructionOnActivity(path.getPathInstructions().get(selectedStep),selectedStep+1);
            view.showInstructionOnMap(path.getPathInstructions().get(selectedStep),path);
        }
    }

    @Override
    public void goToPreviousInstruction() {
        if (selectedStep-1>=0)
        {
            selectedStep--;
            view.showInstructionOnActivity(path.getPathInstructions().get(selectedStep),selectedStep+1);
            view.showInstructionOnMap(path.getPathInstructions().get(selectedStep),path);
        }

    }

    @Override
    public void onMapReady() {
        view.showPathOnMap(path);
        view.showInstructionOnMap(path.getPathInstructions().get(selectedStep),path);
    }

    @Override
    public void onCreate() {
        view.showInstructionOnActivity(path.getPathInstructions().get(selectedStep),selectedStep+1);
    }
}
