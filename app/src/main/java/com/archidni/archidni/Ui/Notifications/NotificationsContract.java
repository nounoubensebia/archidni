package com.archidni.archidni.Ui.Notifications;

import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

public interface NotificationsContract {
    public interface View {
        void showNotifications (ArrayList<Notification> allLinesNotifications,
                                ArrayList<Notification> favoritesNotifications);
        void hideLoadingLayout();
        void showLoadingLayout();
    }

    public interface Presenter {
        void onUpdateClicked ();
        void onActivityReady();
    }
}
