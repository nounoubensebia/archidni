package com.archidni.archidni.Ui.Notifications;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.Notifications.NotificationsRepository;
import com.archidni.archidni.Data.Notifications.NotificationsRepositoryImplementation;
import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Notifications.NotificationsUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

public class NotificationsPresenter implements NotificationsContract.Presenter {

    private NotificationsContract.View view;
    private ArrayList<Notification> notifications;
    private NotificationsRepository notificationsRepository;
    private boolean activityReady = false;
    private boolean searching = false;

    public NotificationsPresenter(NotificationsContract.View view) {
        this.view = view;
        this.notifications = new ArrayList<>();
        this.notificationsRepository = new NotificationsRepositoryImplementation();
    }

    private void findNotifications ()
    {
        view.showLoadingLayout();
        notificationsRepository.getNotifications(new NotificationsRepository.OnNotificationsFound() {
            @Override
            public void onNotificationsFound(ArrayList<Notification> notifications) {
                NotificationsPresenter.this.notifications = notifications;
                view.hideLoadingLayout();
                view.showNotifications(notifications,
                            NotificationsUtils.getFavoritesNotifications(App.getAppContext(),notifications));
                searching = false;
            }

            @Override
            public void onError() {
                view.showErrorMessage();
                view.hideLoadingLayout();
            }
        });
    }

    @Override
    public void onUpdateClicked() {
        if (!searching)
        {
            findNotifications();
            searching = true;
        }
    }

    @Override
    public void onActivityReady() {
        if (!activityReady)
        {
            findNotifications();
            activityReady = true;
            searching = true;
        }
    }
}
