package com.archidni.archidni.Data.Notifications;

public class NotificationsRepositoryImplementation implements NotificationsRepository {

    @Override
    public void getNotifications(OnNotificationsFound onNotificationsFound) {
        NotificationsOnlineDataSource notificationsOnlineDataSource = new NotificationsOnlineDataSource();
        notificationsOnlineDataSource.getNotifications(onNotificationsFound);
    }
}
