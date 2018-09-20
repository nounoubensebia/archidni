package com.archidni.archidni.Ui.Report;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportChooseTypeActivity extends AppCompatActivity {

    @BindView(R.id.radio_group_report_type)
    RadioGroup reportTypeRadioGroup;
    @BindView(R.id.text_next)
    TextView nextText;
    @BindView(R.id.text_cancel)
    TextView cancelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_report_type);
        ButterKnife.bind(this);
        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (reportTypeRadioGroup.getCheckedRadioButtonId()==R.id.radio_button_disruptions)
                    intent = new Intent(ReportChooseTypeActivity.this,ReportAlertChooseLineActivity.class);
                else
                    intent = new Intent(ReportChooseTypeActivity.this,ReportInformationExplainProblemActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
