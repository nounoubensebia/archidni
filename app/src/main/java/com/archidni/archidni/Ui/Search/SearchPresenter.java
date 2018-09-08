package com.archidni.archidni.Ui.Search;

import android.content.Context;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.GeoRepository;
import com.archidni.archidni.Data.LineStationSuggestions.LineStationSuggestionsRepository;
import com.archidni.archidni.Data.Station.StationDataRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.CommonPlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.GpsSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.StationSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.StringSimilarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Created by noure on 02/02/2018.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private GeoRepository geoRepository;
    private SearchContract.View view;
    private int requestType;
    private PathPlace bundledPlace;
    private Coordinate userLocation;
    private LineStationSuggestionsRepository lineStationSuggestionsRepository;

    public SearchPresenter(SearchContract.View view,int requestType,PathPlace place) {
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
        lineStationSuggestionsRepository = new LineStationSuggestionsRepository();
        loadSearchResults(null,"");
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
        loadSearchResults(null,"");
    }

    @Override
    public void loadSearchResults(final Context context, final String text) {
        if (lineStationSuggestionsRepository==null)
            lineStationSuggestionsRepository = new LineStationSuggestionsRepository();
        final ArrayList<PlaceSuggestion> placeSuggestions = new ArrayList<>();
        if (text.length()>0)
        {
            view.showPlacesSearchLoadingBar();
            geoRepository.getTextAutoCompleteSuggestions(context,text,
                    new GeoRepository.OnPlaceSuggestionsSearchComplete() {
                        @Override
                        public void onResultsFound(ArrayList<TextQuerySuggestion> textQuerySuggestions) {
                            placeSuggestions.addAll(textQuerySuggestions);

                            lineStationSuggestionsRepository.getStationSuggestions(context, text, new LineStationSuggestionsRepository.OnSearchComplete() {
                                @Override
                                public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                                    ArrayList<PlaceSuggestion> stationSuggestions = new ArrayList<>();
                                    stationSuggestions.addAll(getPlaceSuggestionsFromStations(lineStationSuggestions));
                                    sortSearchSuggestions(text,stationSuggestions);
                                    placeSuggestions.addAll(stationSuggestions);
                                    view.showSearchResults(placeSuggestions);
                                }

                                @Override
                                public void onError() {
                                    view.showSearchResults(placeSuggestions);
                                }
                            });
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

    private ArrayList<PlaceSuggestion> getPlaceSuggestionsFromStations (ArrayList<LineStationSuggestion> lineStationSuggestions)
    {
        ArrayList<PlaceSuggestion> placeSuggestions = new ArrayList<>();
        for (LineStationSuggestion lineStationSuggestion:lineStationSuggestions)
        {
            placeSuggestions.add(new StationSuggestion(lineStationSuggestion.getName(),
                    lineStationSuggestion.getId(),lineStationSuggestion.getTransportMean()));
        }
        return placeSuggestions;
    }

    private void sortSearchSuggestions (final String text, ArrayList<PlaceSuggestion> placeSuggestions)
    {
        Collections.sort(placeSuggestions, new Comparator<PlaceSuggestion>() {
            @Override
            public int compare(PlaceSuggestion placeSuggestion, PlaceSuggestion t1) {

                return (int)(StringSimilarity.similarity(placeSuggestion.getMainText(),text)-
                        StringSimilarity.similarity(t1.getMainText(),text)*1000);
            }
        });
        if (placeSuggestions.size()>3)
        {
            while (placeSuggestions.size()>3)
                placeSuggestions.remove(3);
        }
    }

    @Override
    public void loadPlaceDetails(Context context,PlaceSuggestion placeSuggestion) {

        if (placeSuggestion instanceof TextQuerySuggestion)
        {
            view.showPlaceDetailsLoadingBar();
            geoRepository.getPlaceDetails(context,(TextQuerySuggestion)placeSuggestion,
                    new GeoRepository.OnPlaceDetailsSearchComplete() {
                        @Override
                        public void onResultFound(PathPlace place) {
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
            if (placeSuggestion.getType()==GpsSuggestion.TYPE_SELECT_ON_MAP)
            {
                view.startSetLocationActivity();
            }
            else
            {
                if (userLocation!=null)
                {
                    PathPlace userPlace = new PathPlace("Ma position",
                            userLocation);
                    view.startAskingActivity(requestType,userPlace);
                }
                else
                {
                    view.showMyPositionErrorMsg();
                }
            }
        }
        if (placeSuggestion instanceof CommonPlaceSuggestion)
        {
            view.startAskingActivity(requestType,null);
        }
        if (placeSuggestion instanceof StationSuggestion)
        {
            StationSuggestion stationSuggestion = (StationSuggestion)placeSuggestion;
            view.showPlaceDetailsLoadingBar();
            StationDataRepository stationDataRepository = new StationDataRepository();
            stationDataRepository.getStation(context, new LineStationSuggestion(stationSuggestion.getMainText(),
                            stationSuggestion.getId(), -1),
                    new StationDataRepository.OnSearchComplete() {
                        @Override
                        public void onSearchComplete(Station station) {
                            PathPlace place = new PathPlace(station.getName(),station.getCoordinate());
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

    }

    @Override
    public void onSetMarkerResult(Coordinate coordinate) {
        PathPlace selectedLocation = new PathPlace(StringUtils.getLocationString(coordinate),
                coordinate);
        switch (requestType) {
            case IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_DESTINATION :
                view.startPathSearchActivity(bundledPlace,selectedLocation);
                break;
            case IntentUtils.SearchIntents.TYPE_LOOK_ONLY_FOR_ORIGIN:
                view.startPathSearchActivity(selectedLocation,bundledPlace);
                break;
                default:view.startAskingActivity(requestType,selectedLocation);
        }
    }

    @Override
    public void onUserLocationCaptured(Coordinate userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public void onStop(Context context) {
        geoRepository.cancelRequests(context);
    }
}
