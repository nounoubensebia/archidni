package com.archidni.archidni.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TarifsActivity extends AppCompatActivity {

    @BindView(R.id.bus_button)
    View busButton;
    @BindView(R.id.button_metro)
    View metroButton;
    @BindView(R.id.button_tramway)
    View tramwayButton;
    @BindView(R.id.button_train)
    View trainButton;
    @BindView(R.id.telepheric_button)
    View telephericButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifs);
        ButterKnife.bind(this);
        busButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarifsActivity.this,BusTarifsActivity.class);
                startActivity(intent);
            }
        });

        metroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarifsActivity.this,MetroTarifsActivity.class);
                startActivity(intent);
            }
        });

        tramwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarifsActivity.this,TramwayTarifsActivity.class);
                startActivity(intent);
            }
        });

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarifsActivity.this,TrainTarifsActivity.class);
                startActivity(intent);
            }
        });
        telephericButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarifsActivity.this,TelephericTarifsActivity.class);
                startActivity(intent);
            }
        });
    }
}
