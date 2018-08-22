package com.archidni.archidni.Data.Station;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Station;

import org.json.JSONException;
import org.json.JSONObject;

public class StationParser {

    public Station parseStation (JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        int transportMeanId = jsonObject.getInt("transport_mode_id")-1;
        double latitude = jsonObject.getDouble("latitude");
        double longitude = jsonObject.getDouble("longitude");
        return new Station(id,name,transportMeanId,new Coordinate(latitude,
                longitude));
    }
}
