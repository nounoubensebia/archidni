package com.archidni.archidni.Ui.Report;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesOnlineDataStore;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Reports.DisruptionSubject;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.DisruptionSubjectAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAlertChooseLineActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_alert_choose_line);
        ButterKnife.bind(this);
        populateList();
    }

    private void populateList ()
    {
        LinesAndPlacesOnlineDataStore linesAndPlacesOnlineDataStore = new LinesAndPlacesOnlineDataStore();
        linesAndPlacesOnlineDataStore.getLines(new LinesAndPlacesOnlineDataStore.OnLinesSearchCompleted() {
            @Override
            public void onLinesFound(ArrayList<Line> lines) {
                progressBar.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                },250);
                ArrayList<DisruptionSubject> disruptionSubjects = new ArrayList<>();
                for (Line line:lines)
                {
                    disruptionSubjects.add(new DisruptionSubject(line));
                }
                Collections.sort(disruptionSubjects, new Comparator<DisruptionSubject>() {
                    @Override
                    public int compare(DisruptionSubject disruptionSubject, DisruptionSubject t1) {
                        return disruptionSubject.getLine().getName().compareTo(t1.getLine().getName());
                    }
                });
                for (TransportMean transportMean:TransportMean.allTransportMeans)
                {
                    disruptionSubjects.add(transportMean.getId(),new DisruptionSubject(transportMean));
                }
                DisruptionSubjectAdapter disruptionSubjectAdapter = new DisruptionSubjectAdapter(ReportAlertChooseLineActivity.this
                        , disruptionSubjects, new DisruptionSubjectAdapter.OnItemClick() {
                    @Override
                    public void onItemClick(DisruptionSubject disruptionSubject) {
                        Intent intent = new Intent(ReportAlertChooseLineActivity.this,ReportInformationExplainProblemActivity.class);
                        intent.putExtra(IntentUtils.DISRUPTION_SUBJECT,new Gson().toJson(disruptionSubject));
                        startActivity(intent);
                        finish();
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(ReportAlertChooseLineActivity.this,
                        LinearLayoutManager.VERTICAL,false));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(disruptionSubjectAdapter);
            }

            @Override
            public void onError() {
                Toast.makeText(ReportAlertChooseLineActivity.this,R.string.error_happened,Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
