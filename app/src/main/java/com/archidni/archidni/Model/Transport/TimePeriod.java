package com.archidni.archidni.Model.Transport;

/**
 * Created by noure on 10/02/2018.
 */

public class TimePeriod {
    private long start;
    private long end;
    private int averageWaitingTime;

    public TimePeriod(long start, long end, int averageWaitingTime) {
        this.start = start;
        this.end = end;
        this.averageWaitingTime = averageWaitingTime;
    }
}
