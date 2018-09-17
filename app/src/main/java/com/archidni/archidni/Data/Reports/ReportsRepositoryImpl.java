package com.archidni.archidni.Data.Reports;

import com.archidni.archidni.Model.Reports.DisruptionReport;
import com.archidni.archidni.Model.Reports.PathReport;
import com.archidni.archidni.Model.Reports.Report;

public class ReportsRepositoryImpl implements ReportsRepository {



    @Override
    public void sendDisruptionReport(DisruptionReport disruptionReport, OnCompleteListener onComplete) {
        ReportsDataStore reportsDataStore = new ReportsOnlineDataStore();
        reportsDataStore.sendDisruptionReport(disruptionReport,onComplete);
    }

    @Override
    public void sendOtherReport(Report report, OnCompleteListener onComplete) {
        ReportsDataStore reportsDataStore = new ReportsOnlineDataStore();
        reportsDataStore.sendOtherReport(report,onComplete);

    }

    @Override
    public void sendPathReport(PathReport report, OnCompleteListener onComplete) {
        ReportsDataStore reportsDataStore = new ReportsOnlineDataStore();
        reportsDataStore.sendPathReport(report,onComplete);

    }
}
