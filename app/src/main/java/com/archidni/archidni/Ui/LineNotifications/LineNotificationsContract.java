package com.archidni.archidni.Ui.LineNotifications;

import com.archidni.archidni.Model.Notifications.Notification;

import java.util.ArrayList;

public interface LineNotificationsContract {
    public interface View {
        void showLoadingLayout();
        void hideLoadingLayout();
        void showNotificationsOnList (ArrayList<Notification> notifications);
        void showErrorMessage();
    }

    public interface Presenter {
        void onRefreshClicked ();
        void onActivityReady();
    }
}
