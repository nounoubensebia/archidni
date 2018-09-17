package com.archidni.archidni.Data.Reports;

import com.archidni.archidni.Model.Reports.DisruptionReport;
import com.archidni.archidni.Model.Reports.PathReport;
import com.archidni.archidni.Model.Reports.Report;

public interface ReportsDataStore {

    public void sendDisruptionReport (DisruptionReport disruptionReport, ReportsRepository.OnCompleteListener onComplete);

    public void sendOtherReport (Report report, ReportsRepository.OnCompleteListener onComplete);

    public void sendPathReport (PathReport report, ReportsRepository.OnCompleteListener onComplete);
}
