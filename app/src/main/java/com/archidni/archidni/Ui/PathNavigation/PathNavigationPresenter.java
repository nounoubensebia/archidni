package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 16/02/2018.
 */

public class PathNavigationPresenter implements PathNavigationContract.Presenter {
    private PathNavigationContract.View view;
    private Path path;
    private int selectedStep;

    private LinesAndPlacesRepository linesAndPlacesRepository;

    public PathNavigationPresenter(PathNavigationContract.View view, Path path,int selectedStep) {
        this.view = view;
        this.path = path;
        this.selectedStep = selectedStep;
        linesAndPlacesRepository = new LinesAndPlacesRepository();
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

    @Override
    public void onWaitLineClick(WaitLine waitLine) {
        view.showLineSearchDialog();
        linesAndPlacesRepository.getLine(App.getAppContext(), new LineStationSuggestion(waitLine.getLine().getName(), waitLine.getLine().getId(),
                waitLine.getLine().getTransportMean().getId()), new LinesAndPlacesRepository.OnLineSearchCompleted() {
            @Override
            public void onLineFound(Line line) {
                view.hideLineSearchDialog();
                view.startLineActivity(line);
            }

            @Override
            public void onError() {
                view.hideLineSearchDialog();
                view.showErrorMessage();
            }
        });
    }

    @Override
    public void onStationClick(Station station) {
        view.startStationActivity(station);
    }
}
