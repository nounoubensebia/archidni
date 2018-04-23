package com.archidni.archidni.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsOfUseActivity extends AppCompatActivity {

    @BindView(R.id.button_accept)
    Button acceptButton;

    @BindView(R.id.button_refuse)
    Button refuseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        ButterKnife.bind(this);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(1,"test@test.com","Utilisateur de test","Utilisateur de test");
                SharedPrefsUtils.saveString(TermsOfUseActivity.this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,user.toJson());
                Intent intent = new Intent(TermsOfUseActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TermsOfUseActivity.this,
                        "Vous devez accepter les termes et les conditions d'utilisation de l'application pour pouvoir utiliser l'application",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
