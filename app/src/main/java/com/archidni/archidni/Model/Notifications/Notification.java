package com.archidni.archidni.Model.Notifications;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.TransportMean;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {

    private String title;
    private TransportMean transportMean;
    private ArrayList<Line> lines;
    private String description;

    public Notification(String title, TransportMean transportMean, ArrayList<Line> lines, String description) {
        this.title = title;
        this.transportMean = transportMean;
        this.lines = lines;
        this.description = description;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }

    public String getDescription() {
        return description;
    }

    public String getTitleText ()
    {
        if (lines.size()>0)
        {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            for (Line line:lines)
            {
                stringBuilder.append(line.getName());
                if (i<lines.size()-1)
                    stringBuilder.append(System.getProperty ("line.separator"));
                i++;
            }
            return stringBuilder.toString();
        }
        else
        {
            return transportMean.getName();
        }
    }

    public static Notification fromJson (String json)
    {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String title = jsonObject.getString("title");
            int transportModeId = jsonObject.getInt("transport_mode_id")-1;
            String description = jsonObject.getString("description");
            JSONArray linesJsonArray = jsonObject.getJSONArray("lines");
            ArrayList<Line> lines = new ArrayList<>();
            for (int j=0;j<linesJsonArray.length();j++)
            {
                JSONObject lineJson = linesJsonArray.getJSONObject(j);
                int id = lineJson.getInt("id");
                String name = lineJson.getString("name");
                int transportMeanId = lineJson.getInt("transport_mode_id")-1;
                Line line = new Line(id,name, TransportMean.allTransportMeans.get(transportMeanId));
                lines.add(line);
            }
            Notification notification = new Notification(title,
                    TransportMean.allTransportMeans.get(transportModeId),lines,description);
            return notification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }
}
