package com.archidni.archidni.Model.PlaceSuggestion;

import com.archidni.archidni.R;
import com.google.gson.Gson;

/**
 * Created by noure on 02/02/2018.
 */

public class TextQuerySuggestion extends PlaceSuggestion {

    private String placeId;
    public static int TYPE_LOCATION = 0;
    public static int TYPE_BUILDING = 1;
    public static int TYPE_CHOOSE_ON_MAP = 3;
    public static int TYPE_USER_SUGGESTION = 4;


    public TextQuerySuggestion(String mainText, String secondaryText, int type, String placeId) {
        super(mainText, secondaryText, type);
        this.placeId = placeId;
    }

    @Override
    public int getDrawable() {
        if (type == TYPE_LOCATION)
        {
            return R.drawable.ic_suggestion_location_green_24dp;
        }
        else
        {
            return R.drawable.ic_suggetstion_building_green_24dp;
        }
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }


    public static TextQuerySuggestion fromJson(String json) {
        return new Gson().fromJson(json,TextQuerySuggestion.class);
    }

    public String getPlaceId() {
        return placeId;
    }
}
