package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;

import java.io.Serializable;

/**
 * Created by noure on 03/02/2018.
 */

public class WaitInstruction extends PathInstruction implements Serializable {

    private Coordinate coordinate;
    private boolean isAverage = false;
    private RideInstruction rideInstruction;


    public WaitInstruction(int duration, Coordinate coordinate) {
        super(duration);
        this.coordinate = coordinate;
    }


    @Override
    public String getMainText() {
        if (rideInstruction.getTransportMean().getId()!= TransportMean.ID_BUS)
        return "Attendre le "+rideInstruction.getTransportMean().getName()+
                " ligne : "+rideInstruction.getLineLabel()+" vers "+rideInstruction.getTerminus();
        else
            return "Attendre le "+rideInstruction.getTransportMean().getName()+
                    " "+rideInstruction.getLineLabel()+" vers "+rideInstruction.getTerminus();
    }

    @Override
    public String getSecondaryText() {
        if (isAverage) return ("Temps d'attente moyen "+ getDuration()/60+" minutes");
        else return (getDuration()/60+" minutes");
    }

    @Override
    public long getInstructionIcon() {
        return R.drawable.ic_time_transport_mean_2 ;
    }

    @Override
    public int getInstructionWhiteIcon() {
        return R.drawable.ic_time_white_24dp;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAverage(boolean average) {
        this.isAverage = average;
    }

    public void setRideInstruction(RideInstruction rideInstruction) {
        this.rideInstruction = rideInstruction;
    }
}
