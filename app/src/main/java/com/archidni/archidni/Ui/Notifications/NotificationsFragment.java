package com.archidni.archidni.Ui.Notifications;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archidni.archidni.Data.SharedPrefsUtils;
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

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.text_no_favorites)
    TextView noFavoritesText;

    OnReadyListener onReadyListener;

    boolean isFavoritesFragment;


    @SuppressLint("ValidFragment")
    public NotificationsFragment(boolean isFavoritesFragment,OnReadyListener onReadyListener) {
        super();
        this.onReadyListener = onReadyListener;
        this.isFavoritesFragment = isFavoritesFragment;
    }

    public void setOnReadyListener(OnReadyListener onReadyListener) {
        this.onReadyListener = onReadyListener;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_notifications, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this,getView());
        onReadyListener.onReady();
    }

    public void showNotifications(ArrayList<Notification> notifications)
    {
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),notifications);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        notificationsList.setLayoutManager(mLayoutManager);
        notificationsList.setItemAnimator(new DefaultItemAnimator());
        notificationsList.setAdapter(notificationAdapter);
        if (!SharedPrefsUtils.favoritesAdded(getContext())&&isFavoritesFragment)
        {
            noFavoritesText.setVisibility(View.VISIBLE);
        }
        else
        {
            noFavoritesText.setVisibility(View.GONE);
        }
    }

    public void hideLoadingLayout() {
        progressBar.setVisibility(View.GONE);
        notificationsList.setVisibility(View.VISIBLE);
    }

    public void showLoadingLayout() {
        notificationsList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }


    public interface OnReadyListener {
        public void onReady();
    }

}
