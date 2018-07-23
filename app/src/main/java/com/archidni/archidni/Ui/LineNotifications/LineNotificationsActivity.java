package com.archidni.archidni.Ui.LineNotifications;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.NotificationAdapter;
import com.archidni.archidni.Ui.Notifications.NotificationsFragment;
import com.archidni.archidni.Ui.SearchLineStation.LineFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineNotificationsActivity extends AppCompatActivity implements LineNotificationsContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView notificationsList;

    private LineNotificationsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_notifications);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        Line line = Line.fromJson(extras.getString(IntentUtils.LINE_LINE));
        presenter = new LineNotificationsPresenter(this,line);
    }

    @Override
    public void showLoadingLayout() {
        notificationsList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingLayout() {
        notificationsList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNotificationsOnList(ArrayList<Notification> notifications) {
        NotificationAdapter notificationAdapter = new NotificationAdapter(this,notifications);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        notificationsList.setLayoutManager(mLayoutManager);
        notificationsList.setItemAnimator(new DefaultItemAnimator());
        notificationsList.setAdapter(notificationAdapter);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,"Une erreur s'est produite veuillez réessayez",Toast.LENGTH_LONG).show();
    }


}
