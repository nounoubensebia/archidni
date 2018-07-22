package com.archidni.archidni.Data.Notifications;

import com.archidni.archidni.Model.Notifications.Notification;

import java.util.ArrayList;

public interface NotificationsRepository {


    public void getNotifications (OnNotificationsFound onNotificationsFound);


    public interface OnNotificationsFound {
        void onNotificationsFound (ArrayList<Notification> notifications);
        void onError();
    }

}
