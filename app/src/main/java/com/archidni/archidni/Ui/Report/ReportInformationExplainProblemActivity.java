package com.archidni.archidni.Ui.Report;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        final Bundle extras = getIntent().getExtras();
        reportsRepository = new ReportsRepositoryImpl();
        if (extras!=null&&extras.containsKey(IntentUtils.DISRUPTION_SUBJECT))
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
            if (extras!=null&&extras.containsKey(IntentUtils.PATH))
            {
                leaveEmptyText.setVisibility(View.VISIBLE);
                sendText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Path path = Path.fromJson(extras.getString(IntentUtils.PATH));
                        boolean isGood = extras.getBoolean(IntentUtils.IS_PATH_GOOD);
                        sendPathReport(path,isGood);
                    }
                });

            }
            else
            {
                leaveEmptyText.setVisibility(View.GONE);
                sendText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String description = problemText.getText().toString();
                        if (description.equals(""))
                        {
                            Toast.makeText(ReportInformationExplainProblemActivity.this,
                                    "Veuillez donner une description du problème rencontré!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            sendOtherReport();
                        }
                    }
                });

            }

            problemText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length()>0)
                    {
                        leaveEmptyText.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (extras!=null&&extras.containsKey(IntentUtils.PATH))
                        {
                            leaveEmptyText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
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
                        R.string.report_sent,Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
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
                        R.string.report_sent,Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
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
                        R.string.report_sent,Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError() {
                progressDialog.hide();
                Toast.makeText(ReportInformationExplainProblemActivity.this,
                        R.string.error_happened,Toast.LENGTH_LONG).show();
            }
        });
    }
}
