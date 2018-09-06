package com.archidni.archidni.Model.Notifications;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.TransportMean;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {

    private String title;
    private TransportMean transportMean;
    private ArrayList<LineSkeleton> lines;
    private String description;
    private int type;

    public static final int TYPE_PERTURBATION = 1;
    public static final int TYPE_NORMAL_ALERT = 2;

    public Notification(String title, TransportMean transportMean, ArrayList<LineSkeleton> lines, String description, int type) {
        this.title = title;
        this.transportMean = transportMean;
        this.lines = lines;
        this.description = description;
        this.type = type;
    }

    public ArrayList<LineSkeleton> getLines() {
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
            for (LineSkeleton line:lines)
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
            int type = jsonObject.getInt("type");
            int transportModeId = jsonObject.getInt("transport_mode_id")-1;
            String description = jsonObject.getString("description");
            JSONArray linesJsonArray = jsonObject.getJSONArray("lines");
            ArrayList<LineSkeleton> lines = new ArrayList<>();
            for (int j=0;j<linesJsonArray.length();j++)
            {
                JSONObject lineJson = linesJsonArray.getJSONObject(j);
                int id = lineJson.getInt("id");
                String name = lineJson.getString("name");
                int transportMeanId = lineJson.getInt("transport_mode_id")-1;
                LineSkeleton line = new LineSkeleton(id,name, TransportMean.allTransportMeans.get(transportMeanId));
                lines.add(line);
            }
            Notification notification = new Notification(title,
                    TransportMean.allTransportMeans.get(transportModeId),lines,description,type);
            return notification;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
