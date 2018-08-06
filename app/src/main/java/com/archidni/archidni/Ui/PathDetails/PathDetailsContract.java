package com.archidni.archidni.Ui.PathDetails;

import android.content.Context;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;

/**
 * Created by noure on 12/02/2018.
 */

public interface PathDetailsContract {
    public interface View {
        void showPathOnActivity(Path path);
        void showPathOnMap(Path path);
        void startPathNavigationActivity(Path path);
        void showLineSearchDialog ();
        void hideLineSearchDialog ();
        void startLineActivity (Line line);
        void showLineSearchError ();
    }

    public interface Presenter {
        void onCreate();
        void onMapReady();
        void onStartNavigationClick();
        void onLineItemClick (Context context, LineSkeleton lineSkeleton);
    }
}
