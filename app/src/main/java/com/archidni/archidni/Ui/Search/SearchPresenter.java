package com.archidni.archidni.Ui.Search;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.GeoRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.PlaceSuggestion.CommonPlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.GpsSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private GeoRepository geoRepository;
    private SearchContract.View view;
    private int requestType;
    private Place bundledPlace;

    public SearchPresenter(SearchContract.View view,int requestType,Place place) {
        this.view = view;
        this.requestType = requestType;
        this.bundledPlace = place;
        if (requestType== IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_DESTINATION)
        {
            view.showHintMessage(App.getAppContext().getString(R.string.where_to_go));
        }
        else
        {
            view.showHintMessage(App.getAppContext().getString(R.string.where_do_you_come_from));
        }
        geoRepository = new GeoRepository();
        loadSearchResults("");
    }

    public SearchPresenter (SearchContract.View view,int requestType)
    {
        this.view = view;
        this.requestType = requestType;
        if (requestType== IntentUtils.SearchIntents.TYPE_LOOK_FOR_DEST)
        {
            view.showHintMessage(App.getAppContext().getString(R.string.where_to_go));
        }
        else
        {
            view.showHintMessage(App.getAppContext().getString(R.string.where_do_you_come_from));
        }
        geoRepository = new GeoRepository();
        loadSearchResults("");
    }

    @Override
    public void loadSearchResults(String text) {
        final ArrayList<PlaceSuggestion> placeSuggestions = new ArrayList<>();
        if (text.length()>0)
        {
            view.showPlacesSearchLoadingBar();
            geoRepository.getTextAutoCompleteSuggestions(text,
                    new GeoRepository.OnPlaceSuggestionsSearchComplete() {
                        @Override
                        public void onResultsFound(ArrayList<TextQuerySuggestion> textQuerySuggestions) {
                            placeSuggestions.addAll(textQuerySuggestions);
                            view.showSearchResults(placeSuggestions);
                        }

                        @Override
                        public void onError() {
                            view.showErrorMessage();
                        }
                    });
        }
        else
        {
            if (requestType == IntentUtils.SearchIntents.TYPE_LOOK_FOR_OR ||
                    requestType == IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_ORIGIN)
                placeSuggestions.add(new GpsSuggestion(GpsSuggestion.TYPE_GPS_LOCATION));
            placeSuggestions.addAll(PlaceSuggestion.getHints());
            view.showSearchResults(placeSuggestions);
        }
    }

    @Override
    public void loadPlaceDetails(PlaceSuggestion placeSuggestion) {
        view.showPlaceDetailsLoadingBar();
        if (placeSuggestion instanceof TextQuerySuggestion)
        {
            geoRepository.getPlaceDetails((TextQuerySuggestion)placeSuggestion,
                    new GeoRepository.OnPlaceDetailsSearchComplete() {
                        @Override
                        public void onResultFound(Place place) {
                            if (requestType == IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_DESTINATION)
                            {
                                view.startPathSearchActivity(bundledPlace,place);
                            }
                            else
                            {
                                if (requestType == IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_ORIGIN)
                                {
                                    view.startPathSearchActivity(place,bundledPlace);
                                }
                                else
                                {
                                    view.startAskingActivity(requestType,place);
                                }
                            }


                        }

                        @Override
                        public void onError() {
                            view.startAskingActivity(requestType,null);
                        }
                    });
        }
        if (placeSuggestion instanceof GpsSuggestion)
        {
            //view.startAskingActivity(requestType,null);
            if (placeSuggestion.getType()==GpsSuggestion.TYPE_SELECT_ON_MAP)
            {
                view.startSetLocationActivity();
            }
        }
        if (placeSuggestion instanceof CommonPlaceSuggestion)
        {
            view.startAskingActivity(requestType,null);
        }
    }

    @Override
    public void onSetMarkerResult(Coordinate coordinate) {
        Place selectedLocation = new Place(StringUtils.getLocationString(coordinate),
                App.getAppContext().getString(R.string.on_map),coordinate);
        view.startPathSearchActivity(bundledPlace,selectedLocation);
    }
}
