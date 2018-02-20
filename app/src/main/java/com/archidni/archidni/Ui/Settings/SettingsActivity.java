package com.archidni.archidni.Ui.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.editText_ipadr)
    EditText ipAddressText;
    @BindView(R.id.button_save)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ipAddressText.setText(SharedPrefsUtils.getServerUrl(this));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = ipAddressText.getText().toString();
                SharedPrefsUtils.saveString(SettingsActivity.this,
                        SharedPrefsUtils.SHARED_PREFS_ENTRY_SERVER_URL,newUrl);
                Toast.makeText(SettingsActivity.this,"Opération réussie",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
