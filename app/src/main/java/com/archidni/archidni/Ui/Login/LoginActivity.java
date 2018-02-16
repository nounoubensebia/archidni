package com.archidni.archidni.Ui.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Data.Users.UsersRepository;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.Ui.Signup.SignupActivity;
import com.archidni.archidni.UiUtils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText_password)
    EditText passwordEditText;
    @BindView(R.id.editText_email)
    EditText emailEditText;
    @BindView(R.id.text_login)
    TextView loginText;
    @BindView(R.id.button_signup)
    Button signUpText;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    UsersRepository usersRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usersRepository = new UsersRepository();
        initViews();
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getEditableText().toString();
                String password = passwordEditText.getText().toString();
                if (StringUtils.isValidEmailAddress(email)&&StringUtils.isValidPassword(password))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.GONE);
                    usersRepository.login(LoginActivity.this, email, password,
                            new UsersRepository.LoginRequestCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    progressBar.setVisibility(View.GONE);
                                    loginText.setVisibility(View.VISIBLE);
                                    SharedPrefsUtils.saveString(LoginActivity.this,
                                            SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,user.toJson());
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onEmailOrPasswordIncorrect() {
                                    progressBar.setVisibility(View.GONE);
                                    loginText.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this,"email ou mot de passe incorrect",
                                            Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNetworkError() {
                                    progressBar.setVisibility(View.GONE);
                                    loginText.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this,R.string.error_happened,
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
                }
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
