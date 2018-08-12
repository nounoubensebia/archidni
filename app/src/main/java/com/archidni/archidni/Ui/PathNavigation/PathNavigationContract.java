package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;

/**
 * Created by nouno on 16/02/2018.
 */

public interface PathNavigationContract {
    interface View {
        void showInstructionOnActivity(PathInstruction pathInstruction, int stepCount);
        void showInstructionOnMap(PathInstruction pathStep, Path path);
        void showPathOnMap (Path path);
    }

    interface Presenter {
        void goToNextInstruction();
        void goToPreviousInstruction();
        void onMapReady();
        void onCreate();
    }
}
