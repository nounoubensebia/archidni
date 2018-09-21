package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.TransportMean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by noure on 03/02/2018.
 */

public class Path implements Serializable {

    private PathSettings pathSettings;
    private ArrayList<PathInstruction> pathInstructions;


    public Path(PathSettings pathSettings, ArrayList<PathInstruction> pathInstructions) {
        this.pathSettings = pathSettings;
        this.pathInstructions = pathInstructions;
        int i = 0;
        for (PathInstruction pathInstruction:this.pathInstructions)
        {
            if (pathInstruction instanceof WaitInstruction)
            {
                if (this.pathInstructions.get(i-1) instanceof RideInstruction)
                {
                    ((WaitInstruction)pathInstruction).setTransferWithoutWalking(true);
                }
            }
            i++;
        }
    }

    public Calendar getDepartureTime ()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,pathSettings.getDepartureArrivalTime().get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,pathSettings.getDepartureArrivalTime().get(Calendar.MINUTE));
        if (pathSettings.isArriveBy())
        {
            calendar.setTimeInMillis(calendar.getTimeInMillis()-getDuration()*1000);
        }
        return calendar;
    }

    public Calendar getArrivalTime ()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,pathSettings.getDepartureArrivalTime().get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,pathSettings.getDepartureArrivalTime().get(Calendar.MINUTE));
        if (!pathSettings.isArriveBy())
        {
            calendar.setTimeInMillis(calendar.getTimeInMillis()+getDuration()*1000);
        }
        return calendar;
    }

    public PathPlace getOrigin() {
        return pathSettings.getOrigin();
    }

    public void setOrigin(PathPlace departure) {
        pathSettings.setOrigin(departure);
    }

    public PathPlace getDestination() {
        return pathSettings.getDestination();
    }

    public void setDestination(PathPlace destination) {
        pathSettings.setDestination(destination);
    }

    public PathSettings getPathSettings() {
        return pathSettings;
    }


    public ArrayList<PathInstruction> getPathInstructions() {
        return pathInstructions;
    }

    public long getDuration() {
        long duration = 0;
        for (PathInstruction pathInstruction : pathInstructions) {
            duration += pathInstruction.getDuration();
        }
        return duration;
    }

    public ArrayList<TransportMean> getTransportMeans() {
        ArrayList<TransportMean> transportMeen = new ArrayList<>();
        for (PathInstruction pathInstruction : pathInstructions) {
            if (pathInstruction instanceof RideInstruction) {
                RideInstruction rideInstruction = (RideInstruction) pathInstruction;
                if (!transportMeen.contains(rideInstruction.getTransportMean())) {
                    transportMeen.add(rideInstruction.getTransportMean());
                }
            }
        }
        return transportMeen;
    }

    public String getEtaText(long timeOfDeparture) {
        long timeOfArrival = timeOfDeparture + getDuration();
        long hours = timeOfArrival / 3600;
        long minutes = (timeOfArrival - hours * 3600) / 60;
        return StringUtils.getTimeString(timeOfArrival);
    }

    public long getDurationInMinutes() {
        return (getDuration() / 60);
    }


    public ArrayList<Coordinate> getPolyline() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (PathInstruction pathInstruction : pathInstructions) {
            if (pathInstruction instanceof MoveInstruction) {
                MoveInstruction movementInstruction = (MoveInstruction) pathInstruction;
                coordinates.addAll(movementInstruction.getPolyline());
            }
        }
        return coordinates;
    }


    public long getWalkingTime ()
    {
        long walkingTime = 0;
        for (PathInstruction pathInstruction:pathInstructions)
        {
            if (pathInstruction instanceof WalkInstruction)
            {
                walkingTime+=pathInstruction.getDuration();
            }
        }
        return walkingTime;
    }

    public int getBoardingNumber()
    {
        int transferNumber = 0;
        for (PathInstruction pathInstruction:pathInstructions)
        {
            if (pathInstruction instanceof WaitInstruction)
            {
                transferNumber++;
            }
        }
        return transferNumber;
    }

    public float getScore ()
    {
        long duration = getDuration();
        duration -=getWalkingTime();
        float percentage = (float)(getBoardingNumber()-1)*(float)8/100;
        duration += getWalkingTime()*1.2;
        return (long) (duration + percentage*duration);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Path fromJson (String json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json);
            PathSettings pathSettings = new Gson()
                    .fromJson(jsonObject.getJSONObject("pathSettings").toString(),PathSettings.class);
            JSONArray jsonArray = jsonObject.getJSONArray("pathInstructions");
            ArrayList<PathInstruction> pathInstructions = new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject arrayObj = jsonArray.getJSONObject(i);
                if (arrayObj.has("type"))
                {
                    pathInstructions.add(new Gson().fromJson(arrayObj.toString(),WalkInstruction.class));
                }
                else
                {
                    if (arrayObj.has("coordinate"))
                    {
                        pathInstructions.add(new Gson().fromJson(arrayObj.toString(),WaitInstruction.class));
                    }
                    else
                    {
                        pathInstructions.add(new Gson().fromJson(arrayObj.toString(),RideInstruction.class));
                    }
                }
            }
            return new Path(pathSettings,pathInstructions);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
