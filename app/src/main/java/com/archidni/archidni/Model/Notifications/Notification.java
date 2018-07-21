package com.archidni.archidni.Model.Notifications;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

public class Notification {
    private ArrayList<Line> lines;
    private TransportMean transportMean;
    private String description;

    public Notification(ArrayList<Line> lines, TransportMean transportMean, String description) {
        this.lines = lines;
        this.transportMean = transportMean;
        this.description = description;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }

    public String getDescription() {
        return description;
    }

    public String getTitleText ()
    {
        if (lines.size()>0)
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (Line line:lines)
            {
                stringBuilder.append(line.getName());
            }
            return stringBuilder.toString();
        }
        else
        {
            return transportMean.getName();
        }
    }
}
