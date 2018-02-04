package com.archidni.archidni.Model.Path;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class WalkInstruction extends MoveInstruction {
    private Place destination;

    public WalkInstruction(long durationInSeconds, int order,
                           int distanceInMeters, ArrayList<Coordinate> polyline,
                           Place destination) {
        super(durationInSeconds, order, distanceInMeters, polyline);
        this.destination = destination;
    }

    @Override
    public String getMainText() {
        return App.getAppContext().getString(R.string.walk_to)+" "+destination.getMainText();
    }

    @Override
    public String getSecondaryText() {
        return StringUtils.getTextFromDuration(durationInSeconds)+", "+
                StringUtils.getTextFromDistance(distanceInMeters);
    }
}
