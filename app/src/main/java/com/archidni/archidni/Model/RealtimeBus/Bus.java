package com.archidni.archidni.Model.RealtimeBus;

import com.archidni.archidni.Model.Coordinate;

public class Bus {
    private String id;
    private Coordinate coordinate;
    private long timeStamp;
    private int speed;
    private int course;

    public Bus(String id, Coordinate coordinate, long timeStamp, int speed, int course) {
        this.id = id;
        this.coordinate = coordinate;
        this.timeStamp = timeStamp;
        this.speed = speed;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCourse() {
        return course;
    }
}
