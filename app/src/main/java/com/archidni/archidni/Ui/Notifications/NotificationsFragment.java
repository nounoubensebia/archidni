package com.archidni.archidni.Ui.Notifications;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.NotificationAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class NotificationsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView notificationsList;

    OnReadyListener onReadyListener;

    @SuppressLint("ValidFragment")
    public NotificationsFragment(OnReadyListener onReadyListener) {
        super();
        this.onReadyListener = onReadyListener;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this,rootView);
        onReadyListener.onReady();
        return rootView;
    }


    public void showNotifications(ArrayList<Notification> notifications)
    {
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),notifications);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        notificationsList.setLayoutManager(mLayoutManager);
        notificationsList.setItemAnimator(new DefaultItemAnimator());
        notificationsList.setAdapter(notificationAdapter);
        notificationsList.setHasFixedSize(true);
        notificationsList.setNestedScrollingEnabled(false);
    }



    public interface OnReadyListener {
        public void onReady();
    }

}
