package com.archidni.archidni.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.archidni.archidni.App;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

public class TestActivity extends AppCompatActivity {
    private int _yDelta;
    private int oldY=913;
    private View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mapView = findViewById(R.id.mapView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OauthStringRequest oauthStringRequest = new OauthStringRequest(
                        Request.Method.GET, SharedPrefsUtils.getServerUrl(
                        App.getAppContext()) + "/api/test", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TestActivity.this, "comp", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TestActivity.this, "error", Toast.LENGTH_LONG).show();
                    }
                }
                );
                oauthStringRequest.performRequest("TEST");
            }
        });
    }



}
