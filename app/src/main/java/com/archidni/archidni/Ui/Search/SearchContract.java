package com.archidni.archidni.Ui.Search;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public interface SearchContract {
    interface View {
        void showSearchResults(ArrayList<PlaceSuggestion> placeSuggestions);
        void showErrorMessage();
        void showPlaceDetailsLoadingBar();
        void startAskingActivity(int requestType,PathPlace place);
        void startSetLocationActivity();
        void startPathSearchActivity(PathPlace origin,PathPlace destination);
        void showPlacesSearchLoadingBar();
        void showHintMessage (String message);
        void showMyPositionErrorMsg();
    }

    interface Presenter {
        void loadSearchResults(Context context,String text);
        void loadPlaceDetails(Context context,PlaceSuggestion place);
        void onSetMarkerResult(Coordinate selectedCoordinate);
        void onUserLocationCaptured(Coordinate userLocation);
        void onStop(Context context);
    }

}
