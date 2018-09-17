package com.archidni.archidni.Model.Reports;

import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.Model.User;

public class DisruptionReport extends Report {

    private DisruptionSubject disruptionSubject;


    public DisruptionReport(User user, String description, DisruptionSubject disruptionSubject) {
        super(user, description);
        this.disruptionSubject = disruptionSubject;
    }

    public DisruptionSubject getDisruptionSubject() {
        return disruptionSubject;
    }
}
