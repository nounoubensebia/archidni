package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 12/02/2018.
 */

public class PathStep implements Serializable {
    private String stepInstruction;
    private int iconDrawableId;
    private String stepDetails1;
    private String stepDetails2;
    private ArrayList<Coordinate> polyline;
    private Coordinate coordinate;

    private PathStep (Builder builder){
        this.stepInstruction = builder.stepInstruction;
        this.iconDrawableId = builder.iconDrawableId;
        stepDetails1=builder.stepDetails1;
        stepDetails2=builder.stepDetails2;
        polyline=builder.polyline;
        coordinate = builder.coordinate;
    }

    public int getIconDrawableId() {
        return iconDrawableId;
    }

    public String getStepInstruction() {
        return stepInstruction;
    }

    public String getStepDetails1() {
        return stepDetails1;
    }

    public String getStepDetails2() {
        return stepDetails2;
    }

    public ArrayList<Coordinate> getPolyline() {
        return polyline;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public static class Builder {
        private String stepInstruction;
        private int iconDrawableId;
        private String stepDetails1;
        private String stepDetails2;
        private ArrayList<Coordinate> polyline;
        private Coordinate coordinate;

        public Builder(String stepInstruction,int iconDrawableId) {
            this.stepInstruction = stepInstruction;
            this.iconDrawableId = iconDrawableId;
            polyline = null;
            coordinate = null;
            stepDetails1 = null;
            stepDetails2 = null;
        }

        public void setStepDetails1(String stepDetails1) {
            this.stepDetails1 = stepDetails1;
        }

        public void setStepDetails2(String stepDetails2) {
            this.stepDetails2 = stepDetails2;
        }

        public void setPolyline(ArrayList<Coordinate> polyline) {
            this.polyline = polyline;
        }

        public void setCoordinate(Coordinate coordinate) {
            this.coordinate = coordinate;
        }
        public PathStep build ()
        {
            return new PathStep(this);
        }
    }
}
