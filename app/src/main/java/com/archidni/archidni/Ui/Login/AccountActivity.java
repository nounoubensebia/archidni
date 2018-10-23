package com.archidni.archidni.Ui.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.text_change_password)
    TextView changePasswordText;
    @BindView(R.id.text_update_info)
    TextView updateInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this,
                        ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        updateInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this,
                        ModifyAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
