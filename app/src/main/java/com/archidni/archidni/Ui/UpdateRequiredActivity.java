package com.archidni.archidni.Ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateRequiredActivity extends AppCompatActivity {

    @BindView(R.id.button_update_app)
    Button updateAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_incorrect);
        ButterKnife.bind(this);
        updateAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.archidni.archidni"));
                startActivity(intent);
            }
        });
    }
}
