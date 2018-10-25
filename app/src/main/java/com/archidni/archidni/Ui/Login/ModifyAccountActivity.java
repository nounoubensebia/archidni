package com.archidni.archidni.Ui.Login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Data.Users.UsersRepository;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.UiUtils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyAccountActivity extends AppCompatActivity {

    @BindView(R.id.editText_firstname)
    EditText firstNameEditText;
    @BindView(R.id.editText_lastname)
    EditText lastNameEditText;
    @BindView(R.id.text_finish)
    TextView finishText;
    @BindView(R.id.text_cancel)
    TextView cancelText;

    UsersRepository usersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        usersRepository = new UsersRepository();
        ButterKnife.bind(this);
        User user = SharedPrefsUtils.getConnectedUser(this);
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        finishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameEditText.getEditableText().toString();
                String lastName = lastNameEditText.getEditableText().toString();
                boolean error = false;
                if (!StringUtils.isValidName(firstName))
                {
                    firstNameEditText.setError("Nom non valide");
                    error = true;
                }
                if (!StringUtils.isValidName(lastName))
                {
                    lastNameEditText.setError("Prénom non valide");
                    error = true;
                }
                if (!error)
                {
                    final Dialog progressDialog = DialogUtils.buildProgressDialog("Veuillez patienter",
                            ModifyAccountActivity.this);
                    progressDialog.show();
                    User user = SharedPrefsUtils.getConnectedUser(ModifyAccountActivity.this);
                    user = new User(user.getId(),user.getEmail(),firstName,lastName);
                    usersRepository.updateUserInfo(ModifyAccountActivity.this,
                            user, new UsersRepository.InfoUpdateRequestCallback() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.hide();
                                    Dialog dialog = DialogUtils.buildDialog(ModifyAccountActivity.this,
                                            "Succés", "Changements effectués", "OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(
                                                            ModifyAccountActivity.this,
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
                                public void onNetworkError() {
                                    progressDialog.hide();
                                    Dialog dialog = DialogUtils.buildInfoDialog(ModifyAccountActivity.this,
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
