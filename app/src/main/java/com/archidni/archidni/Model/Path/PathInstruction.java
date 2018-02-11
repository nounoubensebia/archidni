package com.archidni.archidni.Model.Path;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class PathInstruction {
    protected long durationInSeconds;

    public PathInstruction(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }



    public abstract String getMainText();
    public abstract String getSecondaryText();
    public abstract long getInstructionIcon();
    public abstract int getInstructionWhiteIcon();
    public String getDurationString ()
    {
        return getDurationInSeconds()/60+" minutes";
    }
}
