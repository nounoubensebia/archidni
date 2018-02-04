package com.archidni.archidni.Model.Path;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class PathInstruction {
    protected long durationInSeconds;
    protected int order;

    public PathInstruction(long durationInSeconds, int order) {
        this.durationInSeconds = durationInSeconds;
        this.order = order;
    }

    public abstract String getMainText();
    public abstract String getSecondaryText();
}
