package com.archidni.archidni.Model.Transport.Schedule;

public abstract class Schedule {
    private int days;

    public Schedule(int days) {

        this.days = days;
    }


    public int getDays() {
        return days;
    }
}
