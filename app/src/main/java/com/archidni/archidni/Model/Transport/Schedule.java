package com.archidni.archidni.Model.Transport;

public abstract class Schedule {
    private int days;

    public Schedule(int days) {

        this.days = days;
    }


    public int getDays() {
        return days;
    }
}
