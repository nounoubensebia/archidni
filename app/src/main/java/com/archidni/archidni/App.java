package com.archidni.archidni;

import android.app.Application;
import android.content.Context;


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
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
    }
}
