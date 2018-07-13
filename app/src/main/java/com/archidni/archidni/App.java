package com.archidni.archidni;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mapbox.mapboxsdk.Mapbox;

/**
 * Created by noure on 02/02/2018.
 */

public class App extends Application {
    private static Context appContext;

    public static Context getAppContext ()
    {
        return appContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();

        //Mapbox.getInstance(getApplicationContext(), "pk.eyJ1Ijoibm91bm91OTYiLCJhIjoiY2o0Z29mMXNsMDVoazMzbzI1NTJ1MmRqbCJ9.CXczOhM2eznwR0Mv6h2Pgg");

    }
}
