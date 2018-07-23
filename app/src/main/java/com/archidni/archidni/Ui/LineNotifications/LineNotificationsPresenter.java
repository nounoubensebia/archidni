package com.archidni.archidni.Ui.LineNotifications;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesOnlineDataStore;
import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

public class LineNotificationsPresenter implements LineNotificationsContract.Presenter {
    private LineNotificationsContract.View view;
    private Line line;
    private ArrayList<Notification> notifications;
    private LinesAndPlacesRepository linesAndPlacesRepository;

    public LineNotificationsPresenter(LineNotificationsContract.View view, Line line) {
        this.view = view;
        this.line = line;
        linesAndPlacesRepository = new LinesAndPlacesRepository();
    }

    @Override
    public void onRefreshClicked() {
        findNotifications();
    }

    private void findNotifications ()
    {
        view.showLoadingLayout();
        linesAndPlacesRepository.getNotifications(line, new LinesAndPlacesRepository.OnNotificationsFound() {
            @Override
            public void onNotificationsFound(ArrayList<Notification> notifications) {
                view.hideLoadingLayout();
                LineNotificationsPresenter.this.notifications = notifications;
                view.showNotificationsOnList(notifications);
            }

            @Override
            public void onError() {
                view.hideLoadingLayout();
                view.showErrorMessage();
            }
        });
    }

    @Override
    public void onActivityReady() {
        findNotifications();
    }
}
