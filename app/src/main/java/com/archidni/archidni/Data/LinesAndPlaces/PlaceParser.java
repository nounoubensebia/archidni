package com.archidni.archidni.Data.LinesAndPlaces;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceParser {
    private String json;

    public PlaceParser(String json) {
        this.json = json;
    }

    public ArrayList<MainActivityPlace> getPlaces () throws JSONException
    {
        ArrayList<MainActivityPlace> mainActivityPlaces = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            String name = jsonObject.getString("name");
            int capacity = jsonObject.getJSONObject("parking").getInt("capacity");
            long id = jsonObject.getLong("id");
            mainActivityPlaces.add(new Parking(name,new Coordinate(latitude,longitude),(int)id,capacity));
        }
        return mainActivityPlaces;

    }

}
