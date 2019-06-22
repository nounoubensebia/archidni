package com.archidni.archidni.Ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Login.LoginActivity;
import com.archidni.archidni.Ui.Main.MainActivity;
import com.archidni.archidni.Ui.Signup.SignupActivity;
import com.archidni.archidni.UiUtils.DialogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.text_privacy)
    TextView privacyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (!SharedPrefsUtils.isAppDestroyed(App.getAppContext())) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !isGooglePlayServicesAvailable(this)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    DialogUtils.buildClickableInfoDialog(this, "Version incompatible",
                            "Version du système d'exploitation Android incompatible avec" +
                                    " l'application veuillez mettre à jour le système d'exploitation de votre téléphone pour pouvoir utiliser l'application.",
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
            } else {
                int permissionCheck = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                        5);
            }
        }
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

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (!SharedPrefsUtils.verifyKey(this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT))
                    {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this,SignupActivity.class);
                                //startActivity(intent);
                                //finish();
                                //Intent intent = new Intent(SplashActivity.this,TermsOfUseActivity.class);
                                startNextActivity(intent);
                            }
                        },5000);

                    }
                    else
                    {
                        final Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        //startNextActivity(intent);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startNextActivity(intent);
                            }
                        },5000);


                        if (!SharedPrefsUtils.verifyKey(this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUBSCRIBED))
                        {
                            FirebaseMessaging.getInstance().subscribeToTopic("all-devices-v2").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    SharedPrefsUtils.saveString(SplashActivity.this,SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_SUBSCRIBED,
                                            "subscribed");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startNextActivity(intent);
                                        }
                                    },5000);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startNextActivity(intent);
                                        }
                                    },5000);
                                }
                            });
                        }
                        else
                        {
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startNextActivity(intent);
                                }
                            },5000);
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    int permissionCheck = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                            5);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void startNextActivity (final Intent intent)
    {
        startActivity(intent);
        finish();
    }
}
