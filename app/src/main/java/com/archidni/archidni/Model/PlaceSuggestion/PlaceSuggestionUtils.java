package com.archidni.archidni.Model.PlaceSuggestion;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

class PlaceSuggestionUtils {
    static boolean containsType(ArrayList<CommonPlaceSuggestion> commonPlaceSuggestions,
                                int type)
    {
        for (CommonPlaceSuggestion commonPlaceSuggestion:commonPlaceSuggestions)
        {
            if (commonPlaceSuggestion.getType()==type)
            {
                return true;
            }
        }
        return false;
    }

    static CommonPlaceSuggestion getCommonSuggestion(ArrayList<CommonPlaceSuggestion> commonPlaceSuggestions,
                                                     int type)
    {
        for (CommonPlaceSuggestion commonPlaceSuggestion:commonPlaceSuggestions)
        {
            if (commonPlaceSuggestion.getType()==type)
            {
                return commonPlaceSuggestion;
            }
        }
        return null;
    }
}
