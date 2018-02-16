package com.archidni.archidni.Ui.PathDetails;

import com.archidni.archidni.Model.Path.Path;

/**
 * Created by noure on 12/02/2018.
 */

public interface PathDetailsContract {
    public interface View {
        void showPathOnActivity(Path path);
        void showPathOnMap(Path path);
        void startPathNavigationActivity(Path path);
    }

    public interface Presenter {
        void onCreate();
        void onMapReady();
        void onStartNavigationClick();
    }
}
