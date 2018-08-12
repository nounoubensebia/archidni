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

/**
 * Created by noure on 03/02/2018.
 */

public class Path implements Serializable {

    private PathSettings pathSettings;
    private ArrayList<PathInstruction> pathInstructions;


    public Path(PathSettings pathSettings, ArrayList<PathInstruction> pathInstructions) {
        this.pathSettings = pathSettings;
        this.pathInstructions = pathInstructions;
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

    public void setPathSettings(PathSettings pathSettings) {
        this.pathSettings = pathSettings;
    }

    public void setPathInstructions(ArrayList<PathInstruction> pathInstructions) {
        this.pathInstructions = pathInstructions;
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

    public int getTransferNumber ()
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
