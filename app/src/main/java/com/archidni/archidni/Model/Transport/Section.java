package com.archidni.archidni.Model.Transport;

/**
 * Created by noure on 07/02/2018.
 */

public class Section {
    private Station origin;
    private Station destination;

    public Section(Station origin, Station destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }
}
