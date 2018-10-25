package com.archidni.archidni.Ui.Login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.Data.Users.UsersRepository;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.UiUtils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.editText_old_password)
    EditText oldPasswordEditText;
    @BindView(R.id.editText_new_password)
    EditText newPasswordEditText;
    @BindView(R.id.editText_confirm_password)
    EditText confirmPasswordEditText;
    @BindView(R.id.text_finish)
    TextView finishText;
    @BindView(R.id.text_cancel)
    TextView cancelText;

    UsersRepository usersRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usersRepository = new UsersRepository();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                boolean error = false;
                if (!StringUtils.isValidPassword(oldPassword))
                {
                    oldPasswordEditText.setError("Erreur le mot de passe doit contenir plus de 5 " +
                            "caractéres");
                    error = true;
                }
                if (!StringUtils.isValidPassword(newPassword))
                {
                    newPasswordEditText.setError("Erreur le mot de passe doit contenir plus de 5 " +
                            "caractéres");
                    error = true;
                }
                if (!newPassword.equals(confirmPassword))
                {
                    confirmPasswordEditText.setError("Erreur les mots de passes doivent être identiques");
                    error = true;
                }
                if (!error)
                {
                    final Dialog progressDialog = DialogUtils.buildProgressDialog(
                            getResources().getString(R.string.wait),ChangePasswordActivity.this);
                    progressDialog.show();
                    usersRepository.updatePassword(ChangePasswordActivity.this,
                            oldPassword, newPassword,
                            new UsersRepository.PasswordUpdateRequestCallback() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.hide();
                                    Dialog dialog = DialogUtils.buildDialog(ChangePasswordActivity.this,
                                            "Succés", "Changements effectués", "OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(ChangePasswordActivity.this,
                                                            MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                            Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    dialog.show();
                                }

                                @Override
                                public void onOldPasswordIncorrect() {
                                    progressDialog.hide();
                                    Dialog dialog = DialogUtils.buildInfoDialog(ChangePasswordActivity.this,
                                            "Erreur","L'ancien mot de passe est incorrect");
                                    dialog.show();
                                }

                                @Override
                                public void onNetworkError() {
                                    progressDialog.hide();
                                    Dialog dialog = DialogUtils.buildInfoDialog(ChangePasswordActivity.this,
                                            "Erreur",getResources().getString(R.string.error_retry));
                                    dialog.show();
                                }
                            });
                }
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}
