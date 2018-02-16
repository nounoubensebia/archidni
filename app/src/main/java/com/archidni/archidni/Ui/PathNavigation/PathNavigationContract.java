package com.archidni.archidni.Ui.PathNavigation;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathStep;

/**
 * Created by nouno on 16/02/2018.
 */

public interface PathNavigationContract {
    interface View {
        void showStepOnActivity (PathStep pathStep,int stepCount);
        void showStepOnMap (PathStep pathStep, Path path);
        void showPathOnMap (Path path);
    }

    interface Presenter {
        void goToNextStep();
        void goToPreviousStep();
        void onMapReady();
        void onCreate();
    }
}
