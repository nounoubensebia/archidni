package com.archidni.archidni.Ui.LineNotifications;

import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

public interface LineNotificationsContract {
    public interface View {
        void setTheme(Line line);
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
