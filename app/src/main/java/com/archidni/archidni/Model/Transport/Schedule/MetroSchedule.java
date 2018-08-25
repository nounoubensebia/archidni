package com.archidni.archidni.Model.Transport.Schedule;

import com.archidni.archidni.Model.Transport.Schedule.Schedule;

public class MetroSchedule extends Schedule {

    //startTime in seconds since midnight
    private long startTime;
    //endTime in seconds since midnight
    private long endTime;
    //waitingTimeInMinutes;
    private int waitingTime;

    public MetroSchedule(int days, long startTime, long endTime, int waitingTime) {
        super(days);
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitingTime = waitingTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
}
