package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.TransportMean;
import com.google.gson.Gson;

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

    public ArrayList<PathStep> getPathSteps() {
        ArrayList<PathStep> pathSteps = new ArrayList<>();
        for (int i = 0; i < pathInstructions.size(); i++) {
            PathInstruction pathInstruction = pathInstructions.get(i);

            if (pathInstruction instanceof WalkInstruction) {
                String s = "votre destination";
                if ((i + 1) < pathInstructions.size() &&
                        (pathInstructions.get(i + 1) instanceof RideInstruction ||
                                pathInstructions.get(i + 1) instanceof WaitInstruction)) {
                    if (pathInstructions.get(i+1)instanceof RideInstruction)
                        s = "la station de " + ((RideInstruction) pathInstructions.get(i + 1))
                                .getTransportMean().getName() + " "+
                                ((RideInstruction) pathInstructions.get(i + 1)).getSections().get(0)
                                        .getOrigin().getName();
                    else
                        s = "la station de " + ((RideInstruction) pathInstructions.get(i + 2))
                                .getTransportMean().getName() + " "+
                                ((RideInstruction) pathInstructions.get(i + 2)).getSections()
                                        .get(0).getOrigin().getName();
                }
                PathStep.Builder builder = new PathStep.Builder(("Marcher pour atteindre " + s),
                        (int) pathInstruction.getInstructionIcon());
                builder.setPolyline(((WalkInstruction) pathInstruction).getPolyline());
                builder.setStepDetails1(((WalkInstruction) pathInstruction).getDistanceString());
                builder.setStepDetails2(((WalkInstruction) pathInstruction).getDurationString());
                pathSteps.add(builder.build());
            } else {
                if (pathInstruction instanceof WaitInstruction) {
                    PathStep.Builder builder = new PathStep.Builder(((WaitInstruction)pathInstruction).getMainText(),
                            (int) pathInstruction.getInstructionIcon());
                    builder.setStepDetails1(pathInstruction.getDurationString());
                    builder.setCoordinate(((WaitInstruction) pathInstruction).getCoordinate());
                    pathSteps.add(builder.build());
                } else {
                    RideInstruction rideInstruction = (RideInstruction) pathInstruction;
                    PathStep.Builder builder = new PathStep.Builder(rideInstruction.getMainText(),
                            (int) ((RideInstruction) pathInstruction).getTransportMean().getIconEnabled());
                    builder.setStepDetails1(rideInstruction.getDurationString());
                    builder.setStepDetails2(rideInstruction.getSections().size() + " Arrêts");
                    builder.setPolyline(rideInstruction.getPolyline());
                    pathSteps.add(builder.build());
                    builder = new PathStep.Builder(rideInstruction.getExitInstructionText(),
                            (int) ((RideInstruction) pathInstruction).getTransportMean().getExitDrawable());
                    builder.setCoordinate(rideInstruction.getPolyline().get(rideInstruction.getPolyline().size() - 1));
                    pathSteps.add(builder.build());
                }
            }
        }
        return pathSteps;
    }

    public long getWalkingTime ()
    {
        long walkingTime = 0;
        for (PathInstruction pathInstruction:pathInstructions)
        {
            if (pathInstruction instanceof WalkInstruction)
            {
                walkingTime+=pathInstruction.duration;
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
}
