package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by nouno on 16/02/2018.
 */

public interface PathNavigationContract {
    interface View {
        void showInstructionOnActivity(PathInstruction pathInstruction, int stepCount);
        void showInstructionOnMap(PathInstruction pathStep, Path path);
        void showPathOnMap (Path path);
        void startStationActivity(Station station);
        void startLineActivity (Line line);
        void showLineSearchDialog ();
        void hideLineSearchDialog ();
        void showErrorMessage ();
    }

    interface Presenter {
        void goToNextInstruction();
        void goToPreviousInstruction();
        void onMapReady();
        void onCreate();
        void onWaitLineClick (WaitLine waitLine);
        void onStationClick (Station station);
    }
}
