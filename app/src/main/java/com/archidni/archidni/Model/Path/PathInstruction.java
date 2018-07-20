package com.archidni.archidni.Model.Path;

import java.io.Serializable;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class PathInstruction implements Serializable {
    protected long duration;

    public PathInstruction(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }



    public abstract String getMainText();
    public abstract String getSecondaryText();
    public abstract long getInstructionIcon();
    public abstract int getInstructionWhiteIcon();
    public String getDurationString ()
    {
        return (getDuration()/60+" minutes");
    }


}
