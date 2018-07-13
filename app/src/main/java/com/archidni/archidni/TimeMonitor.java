package com.archidni.archidni;

public class TimeMonitor {
    private long startTime;


    private TimeMonitor(long startTime)
    {
        this.startTime = startTime;
    }

    public static TimeMonitor initTimeMonitor ()
    {
        return new TimeMonitor(System.currentTimeMillis());
    }

    public long getElapsedTime()
    {
        return (System.currentTimeMillis()-startTime);
    }
}
