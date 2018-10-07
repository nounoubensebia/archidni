package com.archidni.archidni.Data.RealtimeBus;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.RealtimeBus.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusParser {

    private String json;

    public BusParser(String json) {
        this.json = json;
    }

    public List<Bus> parseBuses  () throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<Bus> buses = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            int speed = jsonObject.getInt("speed");
            int course = jsonObject.isNull("course") ? -1:jsonObject.getInt("course");
            String time = jsonObject.getString("time");
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date date = formatter.parse(time);
                buses.add(new Bus(id,new Coordinate(latitude,longitude),date.getTime(),speed,
                        course));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return buses;
    }
}
