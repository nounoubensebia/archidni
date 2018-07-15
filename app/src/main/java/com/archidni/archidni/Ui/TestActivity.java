package com.archidni.archidni.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        findViewById(R.id.app_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        /*mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });*/
        /*View root = findViewById(R.id.separator);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _yDelta = Y - lParams.bottomMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.bottomMargin = (Y - _yDelta);
                        layoutParams.topMargin = -layoutParams.bottomMargin;
                        float screenHeight = ViewUtils.getScreenHeight(TestActivity.this);
                        Log.e("POSITION",Y+"");
                        boolean moveAuthorized = false;
                        if (oldY>Y)
                        {
                            //moveAuthorized = true;

                            if (ViewUtils.pxToDp(TestActivity.this, (float) Y)>128)
                            {
                                moveAuthorized = true;
                            }
                        }
                        else
                        {

                            //moveAuthorized = true;
                            float marginDown = ViewUtils.getScreenHeight(TestActivity.this)-Y;
                            if (ViewUtils.pxToDp(TestActivity.this,marginDown)>128)
                            {
                                moveAuthorized = true;
                            }
                        }
                        if (moveAuthorized)
                        {
                            view.setLayoutParams(layoutParams);
                            view.animate().translationY(Y - _yDelta).setDuration(0);
                        }
                        break;
                }
                oldY = Y;
                findViewById(R.id.root).invalidate();
                return true;
            }
        });*/
    }



}
