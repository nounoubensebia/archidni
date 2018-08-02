package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class WaitInstruction extends PathInstruction implements Serializable {

    private Coordinate coordinate;
    private ArrayList<WaitLine> waitLines;

    public WaitInstruction(Coordinate coordinate, ArrayList<WaitLine> waitLines) {
        this.coordinate = coordinate;
        this.waitLines = waitLines;
    }

    /*@Override
    public String getMainText() {
        if (rideInstruction.getTransportMean().getId()==4)
        {
            return "Attendre le "+rideInstruction.getTransportMean().getName()+" ligne : "+
                    rideInstruction.getLineLabel()+" vers "+rideInstruction.getTerminus();
        }
        if (rideInstruction.getTransportMean().getId()!= TransportMean.ID_BUS)
        return "Attendre le "+rideInstruction.getTransportMean().getName()+
                " ligne : "+rideInstruction.getLineLabel()+" vers "+rideInstruction.getTerminus();
        else
            return "Attendre le "+rideInstruction.getTransportMean().getName()+
                    " "+rideInstruction.getLineLabel()+" vers "+rideInstruction.getTerminus();
    }*/



    /*@Override
    public String getSecondaryText() {
        if (isAverage) return ("Temps d'attente moyen "+ getDuration()/60+" minutes");
        else return (getDuration()/60+" minutes");
    }*/

    public ArrayList<WaitLine> getWaitLines() {
        return waitLines;
    }

    public long getInstructionIcon() {
        return R.drawable.ic_time_transport_mean_2 ;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }


    public String getTakeLineText ()
    {
        if (waitLines.size()==1)
            return "Prendre la ligne suivante";
        else
            return "Prendre l'une des lignes suivantes";
    }

    @Override
    public String getTtile() {
        return "Attendre le "+waitLines.get(0).getLine().getTransportMean().getName();
    }

    @Override
    public long getDuration() {
        return waitLines.get(0).getTime();
    }
}
