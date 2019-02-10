package com.archidni.archidni.Ui;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Data.Users.UsersRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.UiUtils.DialogUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailVerifActivity extends AppCompatActivity {

    @BindView(R.id.editText_code)
    EditText editTextCode;
    @BindView(R.id.text_verify)
    TextView verifyText;
    @BindView(R.id.text_resend)
    TextView resendText;

    UsersRepository usersRepository;

    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif);
        ButterKnife.bind(this);
        final String email = getIntent().getExtras().getString(IntentUtils.USER_EMAIL);
        final String password = getIntent().getExtras().getString(IntentUtils.USER_PASSWORD);
        usersRepository = new UsersRepository();
        verifyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verifCode = editTextCode.getText().toString();
                progressDialog = DialogUtils.buildProgressDialog("Veuillez patientez",
                        EmailVerifActivity.this);
                progressDialog.show();
                usersRepository.verifyEmailCode(EmailVerifActivity.this, email,
                        password, verifCode, new UsersRepository.EmailVerificationRequestCallback() {
                            @Override
                            public void onSuccess(User user) {
                                progressDialog.hide();
                                SharedPrefsUtils.saveString(EmailVerifActivity.this,
                                        SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,
                                        new Gson().toJson(user));
                                Intent intent = new Intent(EmailVerifActivity.this
                                        , MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCodeIncorrect() {
                                progressDialog.hide();
                                Toast.makeText(EmailVerifActivity.this,
                                        "Code incorrect",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNetworkError() {
                                progressDialog.hide();
                                Toast.makeText(EmailVerifActivity.this,
                                        R.string.error_happened,Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        resendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = DialogUtils.buildProgressDialog("Veuillez patientez",
                        EmailVerifActivity.this);
                progressDialog.show();
                usersRepository.resendVerificationEmail(EmailVerifActivity.this,
                        email, new UsersRepository.EmailVerificationCodeResendCallback() {
                            @Override
                            public void onSuccess() {
                                progressDialog.hide();
                                DialogUtils.buildInfoDialog(EmailVerifActivity.this,
                                        "Succés","Un nouveau code a été envoyé à votre" +
                                                " adresse email").show();
                            }

                            @Override
                            public void onEmailAlreadyVerified() {
                                progressDialog.hide();
                                Toast.makeText(EmailVerifActivity.this,
                                        "Erreur adresse email déjà vérifié",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNetworkError() {
                                progressDialog.hide();
                                Toast.makeText(EmailVerifActivity.this,
                                        R.string.error_happened,Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}
