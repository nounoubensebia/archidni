package com.archidni.archidni.Model.Path;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;

/**
 * Created by noure on 03/02/2018.
 */

public class WaitInstruction extends PathInstruction {

    private Coordinate coordinate;

    public WaitInstruction(long durationInSeconds, int order, Coordinate coordinate) {
        super(durationInSeconds, order);
        this.coordinate = coordinate;
    }

    @Override
    public String getMainText() {
        return App.getAppContext().getString(R.string.wait);
    }

    @Override
    public String getSecondaryText() {
        return StringUtils.getTextFromDuration(durationInSeconds);
    }
}
