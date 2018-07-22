package com.archidni.archidni.Ui.Notifications;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity implements NotificationsContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;
    PgAdapter mPgAdapter;

    @BindView(R.id.fab)
    View fab;


    NotificationsFragment allLinesFragment;
    NotificationsFragment favoritesFragment;
    int fragmentsReady = 0;

    private NotificationsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initViews();
        presenter = new NotificationsPresenter(this);
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPgAdapter = new PgAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPgAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onUpdateClicked();
            }
        });
    }


    @Override
    public void showNotifications(ArrayList<Notification> allLinesNotifications, ArrayList<Notification> favoritesNotifications) {
        allLinesFragment.showNotifications(allLinesNotifications);
        favoritesFragment.showNotifications(favoritesNotifications);
    }

    @Override
    public void hideLoadingLayout() {
       allLinesFragment.hideLoadingLayout();
       favoritesFragment.hideLoadingLayout();
    }

    @Override
    public void showLoadingLayout() {
        allLinesFragment.showLoadingLayout();
        favoritesFragment.showLoadingLayout();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,"Une erreur s'est produite veuillez réessayer",Toast.LENGTH_LONG);
    }


    public class PgAdapter extends FragmentPagerAdapter {

        public PgAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            if (position == 0) {
                if (allLinesFragment==null)
                    allLinesFragment = new NotificationsFragment(new NotificationsFragment.OnReadyListener() {
                        @Override
                        public void onReady() {
                            fragmentsReady++;
                            if (fragmentsReady == 2)
                                presenter.onActivityReady();
                        }
                    });
                return allLinesFragment;
            } else {
                if (favoritesFragment==null)
                    favoritesFragment = new NotificationsFragment(new NotificationsFragment.OnReadyListener() {
                        @Override
                        public void onReady() {
                            fragmentsReady++;
                            if (fragmentsReady == 2)
                                presenter.onActivityReady();
                        }
                    });
                return favoritesFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Toutes les lignes";
            } else {
                return "Lignes favorites";
            }
        }
    }
}
