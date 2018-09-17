package com.archidni.archidni.Ui.Report;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.archidni.archidni.Data.Reports.ReportsRepository;
import com.archidni.archidni.Data.Reports.ReportsRepositoryImpl;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Reports.DisruptionReport;
import com.archidni.archidni.Model.Reports.DisruptionSubject;
import com.archidni.archidni.Model.Reports.PathReport;
import com.archidni.archidni.Model.Reports.Report;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.DialogUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportInformationExplainProblemActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText problemText;
    @BindView(R.id.text_send)
    TextView sendText;
    @BindView(R.id.text_cancel)
    TextView cancelText;
    @BindView(R.id.text_leave_empty)
    TextView leaveEmptyText;

    private Dialog progressDialog;

    private DisruptionSubject disruptionSubject;

    private ReportsRepository reportsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_information_explain_problem);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        reportsRepository = new ReportsRepositoryImpl();
        if (extras.containsKey(IntentUtils.DISRUPTION_SUBJECT))
        {
            disruptionSubject = new Gson().fromJson(extras.getString(IntentUtils.DISRUPTION_SUBJECT),DisruptionSubject.class);
            leaveEmptyText.setVisibility(View.GONE);
            sendText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   sendDisruptionReport(disruptionSubject);
                }
            });
        }
        else
        {
            if (extras.containsKey(IntentUtils.PATH))
            {
                Path path = Path.fromJson(extras.getString(IntentUtils.PATH));
                boolean isGood = extras.containsKey(IntentUtils.IS_PATH_GOOD);
                sendPathReport(path,isGood);
            }
            else
            {
                String description = problemText.getText().toString();
                if (description.equals(""))
                {
                    Toast.makeText(this,"Vous ne pouvez pas laisser le champs problème rencontré vide !",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendOtherReport();
                }
            }
        }

    }

    private void sendDisruptionReport (DisruptionSubject disruptionSubject)
    {

        progressDialog = DialogUtils.buildProgressDialog("Veuillez patientez",this);
        progressDialog.show();
        String description = problemText.getText().toString();
        DisruptionReport disruptionReport = new DisruptionReport(
                SharedPrefsUtils.getConnectedUser(ReportInformationExplainProblemActivity.this),
                description,disruptionSubject);
        reportsRepository.sendDisruptionReport(disruptionReport, new ReportsRepository.OnCompleteListener() {
            @Override
            public void onComplete() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        "Votre feedback a été envoyée merci de votre coopération !",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void sendPathReport (Path path,boolean isGood)
    {

        String description = problemText.getText().toString();
        progressDialog = DialogUtils.buildProgressDialog("Veuillez patientez",this);
        progressDialog.show();
        PathReport pathReport = new PathReport(SharedPrefsUtils.getConnectedUser(this),
                description,path,isGood);
        reportsRepository.sendPathReport(pathReport, new ReportsRepository.OnCompleteListener() {
            @Override
            public void onComplete() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        "Votre feedback a été envoyée merci de votre coopération !",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void sendOtherReport ()
    {
        progressDialog = DialogUtils.buildProgressDialog("Veuillez patientez",this);
        progressDialog.show();
        String description = problemText.getText().toString();
        Report report = new Report(SharedPrefsUtils.getConnectedUser(this),description);
        reportsRepository.sendOtherReport(report, new ReportsRepository.OnCompleteListener() {
            @Override
            public void onComplete() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        "Votre feedback a été envoyée merci de votre coopération !",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
