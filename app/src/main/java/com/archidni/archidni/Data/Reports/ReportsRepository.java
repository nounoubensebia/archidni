package com.archidni.archidni.Data.Reports;

import com.archidni.archidni.Model.Reports.DisruptionReport;
import com.archidni.archidni.Model.Reports.PathReport;
import com.archidni.archidni.Model.Reports.Report;

public interface ReportsRepository {

    public void sendDisruptionReport (DisruptionReport disruptionReport,OnCompleteListener onComplete);

    public void sendOtherReport (Report report,OnCompleteListener onComplete);

    public void sendPathReport (PathReport report,OnCompleteListener onComplete);

    public interface OnCompleteListener {
        void onComplete();
        void onError();
    }

}
