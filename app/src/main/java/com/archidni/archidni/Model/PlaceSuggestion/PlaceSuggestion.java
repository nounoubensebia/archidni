package com.archidni.archidni.Model.PlaceSuggestion;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.UserSuggestions.CommonSuggestionsRepository;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class PlaceSuggestion {
    protected String mainText;
    protected String secondaryText;
    protected int type;

    public PlaceSuggestion(String mainText, String secondaryText, int type) {
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        this.type = type;
    }

    public String getMainText() {
        return mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public abstract int getDrawable();

    public abstract String toJson();

    public int getType() {
        return type;
    }

    public static ArrayList<PlaceSuggestion> getHints ()
    {
        ArrayList<PlaceSuggestion> placeSuggestions = new ArrayList<>();
        placeSuggestions.add(new GpsSuggestion(GpsSuggestion.TYPE_SELECT_ON_MAP));
        ArrayList<CommonPlaceSuggestion> commonPlaceSuggestions = new CommonSuggestionsRepository()
                .getCommonSuggestions(App.getAppContext());
        if (PlaceSuggestionUtils.getCommonSuggestion(commonPlaceSuggestions,
                CommonPlaceSuggestion.TYPE_HOME)!=null)
        {
            placeSuggestions.add(PlaceSuggestionUtils.getCommonSuggestion(commonPlaceSuggestions,
                    CommonPlaceSuggestion.TYPE_HOME));
        }
        else
        {
            placeSuggestions.add(new CommonPlaceSuggestion(App.getAppContext().getString(R.string.home),
                    App.getAppContext().getString(R.string.select_home),
                    CommonPlaceSuggestion.TYPE_HOME,false));
        }

        if (PlaceSuggestionUtils.getCommonSuggestion(commonPlaceSuggestions,
                CommonPlaceSuggestion.TYPE_WORK)!=null)
        {
            placeSuggestions.add(PlaceSuggestionUtils.getCommonSuggestion(commonPlaceSuggestions,
                    CommonPlaceSuggestion.TYPE_WORK));
        }
        else
        {
            placeSuggestions.add(new CommonPlaceSuggestion(App.getAppContext().getString(R.string.work),
                    App.getAppContext().getString(R.string.home),
                    CommonPlaceSuggestion.TYPE_WORK,false));
        }
        return placeSuggestions;
    }

}
