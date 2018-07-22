package com.archidni.archidni.Data.Notifications;

import com.archidni.archidni.Model.Notifications.Notification;

import java.util.ArrayList;

public interface NotificationsDataSource {

    public void getNotifications (NotificationsRepository.OnNotificationsFound onNotificationsFound);

}
