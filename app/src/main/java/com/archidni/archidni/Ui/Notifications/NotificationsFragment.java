package com.archidni.archidni.Ui.Notifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archidni.archidni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends Fragment {
    @BindView(R.id.list_alerts)
    RecyclerView alertsList;
    @BindView(R.id.list_notifications)
    RecyclerView notificationsList;
    @BindView(R.id.image_expand_collapse_alers)
    View expandCollapseAlertsView;
    @BindView(R.id.image_expand_collapse_notifications)
    View expandCollapseNotificationsView;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this,rootView);

        return rootView;
    }
}
