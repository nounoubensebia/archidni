package com.archidni.archidni.Ui.Login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.EmailVerifActivity;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.Ui.Settings.SettingsActivity;
import com.archidni.archidni.Ui.Signup.SignupActivity;
import com.archidni.archidni.Ui.SplashActivity;
import com.archidni.archidni.UiUtils.ActivityUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
    TextView signUpText;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_settings)
    TextView settingsText;
    @BindView(R.id.text_privacy)
    TextView privacyText;

    UsersRepository usersRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getEditableText().toString();
                final String password = passwordEditText.getText().toString();
                if (StringUtils.isValidEmailAddress(email)&&StringUtils.isValidPassword(password))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.GONE);
                    usersRepository.login(LoginActivity.this, email, password,
                            new UsersRepository.LoginRequestCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    SharedPrefsUtils.saveString(LoginActivity.this,
                                            SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,user.toJson());

                                    final Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    if (!SharedPrefsUtils.verifyKey(LoginActivity.this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUBSCRIBED))
                                    {
                                        FirebaseMessaging.getInstance().subscribeToTopic("all-devices-v2").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                SharedPrefsUtils.saveString(LoginActivity.this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUBSCRIBED,
                                                        "subscribed");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        startActivity(intent);
                                    }
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

                                @Override
                                public void onUserAlreadyConnected() {
                                    progressBar.setVisibility(View.GONE);
                                    loginText.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this,"Vous êtes déjà connecté avec un autre appareil",
                                            Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onEmailNotVerified() {
                                    Intent intent = new Intent(LoginActivity.this, EmailVerifActivity.class);
                                    intent.putExtra(IntentUtils.USER_EMAIL,email);
                                    intent.putExtra(IntentUtils.USER_PASSWORD,password);
                                    startActivity(intent);
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
        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://archidni.smartsolutions.network/archidni_privacy_policy.html");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }
}
