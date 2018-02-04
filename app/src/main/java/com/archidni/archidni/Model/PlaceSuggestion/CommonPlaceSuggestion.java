package com.archidni.archidni.Model.PlaceSuggestion;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.R;
import com.google.gson.Gson;

/**
 * Created by noure on 03/02/2018.
 */

public class CommonPlaceSuggestion extends PlaceSuggestion {

    private boolean added;
    private Coordinate coordinate;

    public static int TYPE_HOME = 0;
    public static int TYPE_WORK = 1;

    public CommonPlaceSuggestion(String mainText, String secondaryText, int type, boolean added) {
        super(mainText, secondaryText, type);
        this.added = added;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isAdded() {
        return added;
    }

    @Override
    public int getDrawable() {
        if (type == TYPE_HOME)
        {
            return R.drawable.ic_home_red_24dp;
        }
        else
        {
            return R.drawable.ic_work_black_24dp;
        }
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static CommonPlaceSuggestion fromJson(String json) {
        return new Gson().fromJson(json,CommonPlaceSuggestion.class);
    }
}
