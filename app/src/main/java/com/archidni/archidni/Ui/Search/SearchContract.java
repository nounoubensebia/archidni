package com.archidni.archidni.Ui.Search;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public interface SearchContract {
    interface View {
        void showSearchResults(ArrayList<PlaceSuggestion> placeSuggestions);
        void showErrorMessage();
        void showPlaceDetailsLoadingBar();
        void startAskingActivity(int requestType,Place place);
        void startSetLocationActivity();
        void startPathSearchActivity(Place origin,Place destination);
        void showPlacesSearchLoadingBar();
        void showHintMessage (String message);
        void showMyPositionErrorMsg();
    }

    interface Presenter {
        void loadSearchResults(String text);
        void loadPlaceDetails(PlaceSuggestion place);
        void onSetMarkerResult(Coordinate selectedCoordinate);
        void onUserLocationCaptured(Coordinate userLocation);
    }
}
