package com.archidni.archidni;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessaging;

import static com.archidni.archidni.MyFirebaseMessagingService.CHANNEL_ID;


/**
 * Created by noure on 02/02/2018.
 */

public class App extends Application {
    private static Context appContext;

    public static final int VERSION = 1;

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
        FirebaseMessaging.getInstance().subscribeToTopic("all-devices");
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
