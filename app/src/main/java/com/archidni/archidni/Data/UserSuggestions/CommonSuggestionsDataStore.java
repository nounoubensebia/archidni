package com.archidni.archidni.Data.UserSuggestions;

import android.content.Context;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.PlaceSuggestion.CommonPlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.GpsSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class CommonSuggestionsDataStore {

    private Context context;

    public CommonSuggestionsDataStore(Context context)
    {
        this.context = context;
    }

    public ArrayList<CommonPlaceSuggestion> getCommonSuggestions ()
    {
        ArrayList<CommonPlaceSuggestion> placeSuggestions = new ArrayList<>();
        if (SharedPrefsUtils.verifyKey(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUGGESTIONS))
        {
            String json = SharedPrefsUtils.loadString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUGGESTIONS);
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CommonPlaceSuggestion commonPlaceSuggestion =
                                CommonPlaceSuggestion.fromJson(jsonObject.toString());
                    placeSuggestions.add(commonPlaceSuggestion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return placeSuggestions;
    }


    public void addCommonSuggestion (CommonPlaceSuggestion commonPlaceSuggestion,Context context)
    {
        ArrayList<CommonPlaceSuggestion> placeSuggestions = getCommonSuggestions();
        for (int i=0;i<placeSuggestions.size();i++)
        {
            CommonPlaceSuggestion commonPlaceSuggestion1 = (CommonPlaceSuggestion)placeSuggestions.get(i);
            if (commonPlaceSuggestion1.getType()==commonPlaceSuggestion.getType())
            {
                placeSuggestions.remove(commonPlaceSuggestion1);
                placeSuggestions.add(commonPlaceSuggestion);
                Gson gson = new Gson();
                Type fooType = new TypeToken<ArrayList<CommonPlaceSuggestion>>() {
                }.getType();
                String json = gson.toJson(placeSuggestions,fooType);
                SharedPrefsUtils.saveString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUGGESTIONS,
                        json);
                return;
            }
        }
        placeSuggestions.add(commonPlaceSuggestion);
        Gson gson = new Gson();
        Type fooType = new TypeToken<ArrayList<CommonPlaceSuggestion>>() {
        }.getType();
        String json = gson.toJson(placeSuggestions,fooType);
        SharedPrefsUtils.saveString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUGGESTIONS,
                json);

    }
}
