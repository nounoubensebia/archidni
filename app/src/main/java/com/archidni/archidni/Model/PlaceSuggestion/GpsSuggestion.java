package com.archidni.archidni.Model.PlaceSuggestion;

import com.archidni.archidni.App;
import com.archidni.archidni.R;
import com.google.gson.Gson;

/**
 * Created by noure on 03/02/2018.
 */

public class GpsSuggestion extends PlaceSuggestion {

    public static final int TYPE_SELECT_ON_MAP = 0;
    public static final int TYPE_GPS_LOCATION = 1;

    public GpsSuggestion(int type) {
        super("","",type);
        if (type == TYPE_SELECT_ON_MAP)
        {
            mainText = App.getAppContext().getString(R.string.select_on_map);
            secondaryText = App.getAppContext().getString(R.string.click_to_set_on_map);
        }
        else
        {
            mainText = App.getAppContext().getString(R.string.my_position);
            secondaryText = App.getAppContext().getString(R.string.click_to_set_gps_location);
        }
    }

    public int getType() {
        return type;
    }

    @Override
    public int getDrawable() {
        if (type == TYPE_SELECT_ON_MAP)
        {
            return R.drawable.ic_map_green_24dp;
        }
        else
        {
            return R.drawable.ic_near_me_green_24dp;
        }
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static GpsSuggestion fromJson(String json) {
        return new Gson().fromJson(json,GpsSuggestion.class);
    }
}
