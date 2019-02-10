package com.archidni.archidni.Ui.Signup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Data.Users.UsersRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.EmailVerifActivity;
import com.archidni.archidni.Ui.Login.LoginActivity;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.Ui.Settings.SettingsActivity;
import com.archidni.archidni.UiUtils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.editText_email)
    TextView emailEditText;
    @BindView(R.id.editText_password)
    TextView passwordEditText;
    @BindView(R.id.button_login)
    Button loginText;
    @BindView(R.id.text_signup)
    TextView signupText;
    @BindView(R.id.editText_firstname)
    EditText firstNameEditText;
    @BindView(R.id.editText_lastname)
    EditText lastNameEditText;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_settings)
    TextView settingsText;
    UsersRepository usersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usersRepository = new UsersRepository();
        initViews();
    }

    @Override
    protected void onStop() {
        usersRepository.cancelRequests(getApplicationContext());
        super.onStop();
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getEditableText().toString();
                final String password = passwordEditText.getEditableText().toString();
                String firstName = firstNameEditText.getEditableText().toString();
                String lastName = lastNameEditText.getEditableText().toString();

                if (StringUtils.isValidEmailAddress(email)&&StringUtils.isValidName(firstName)&&
                        StringUtils.isValidName(lastName)&&StringUtils.isValidPassword(password))
                {
                    signupText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    usersRepository.signup(SignupActivity.this, email, password, firstName,
                            lastName,
                            new UsersRepository.SignupRequestCallback() {
                                @Override
                                public void onSuccess() {
                                    /*SharedPrefsUtils.saveString(SignupActivity.this,
                                            SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,user.toJson());
                                    Intent intent = new Intent(SignupActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();*/
                                    Intent intent = new Intent(SignupActivity.this, EmailVerifActivity.class);
                                    intent.putExtra(IntentUtils.USER_EMAIL,email);
                                    intent.putExtra(IntentUtils.USER_PASSWORD,password);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onUserAlreadyExists() {
                                    signupText.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this,
                                            "Un utilisateur avec cette addresse email existe déjà",
                                            Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNetworkError() {
                                    signupText.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this,R.string.error_happened,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                {
                    if (!StringUtils.isValidEmailAddress(email))
                    {
                        emailEditText.setError("Email non valide");
                    }
                    if (!StringUtils.isValidPassword(password))
                    {
                        passwordEditText.setError("Le mot de passe doit contenir plus de 5 caractères");
                    }
                    if (!StringUtils.isValidName(firstName))
                    {
                        firstNameEditText.setError("Prénom non valide");
                    }
                    if (!StringUtils.isValidName(lastName))
                    {
                        lastNameEditText.setError("Nom non valide");
                    }
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }


}
