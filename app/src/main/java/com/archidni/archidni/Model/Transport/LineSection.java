package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;

import java.io.Serializable;

/**
 * Created by noure on 07/02/2018.
 */

public class LineSection extends Section implements Serializable {

    private int mode;
    private int order;

    public LineSection(Station origin, Station destination, int mode, int order) {
        super(origin,destination);
        this.mode = mode;
        this.order = order;
    }

    public int getMode() {
        return mode;
    }

    public int getOrder() {
        return order;
    }



}
