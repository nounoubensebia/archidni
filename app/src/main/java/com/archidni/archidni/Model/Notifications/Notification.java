package com.archidni.archidni.Model.Notifications;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

public class Notification {

    private String title;
    private TransportMean transportMean;
    private ArrayList<Line> lines;
    private String description;

    public Notification(String title, TransportMean transportMean, ArrayList<Line> lines, String description) {
        this.title = title;
        this.transportMean = transportMean;
        this.lines = lines;
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
