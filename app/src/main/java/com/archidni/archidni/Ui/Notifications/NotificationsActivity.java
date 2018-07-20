package com.archidni.archidni.Ui.Notifications;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.archidni.archidni.R;
import com.archidni.archidni.Ui.SearchLineStation.LineFragment;
import com.archidni.archidni.Ui.SearchLineStation.SearchLineStationActivity;
import com.archidni.archidni.Ui.SearchLineStation.StationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;

    PgAdapter mPgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPgAdapter =
                new PgAdapter(
                        getSupportFragmentManager());
        viewPager.setAdapter(mPgAdapter);
    }

    public class PgAdapter extends FragmentPagerAdapter {

        public PgAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                LineFragment lineFragment = new LineFragment();
                return lineFragment;
            } else {
                StationFragment stationFragment = new StationFragment();
                return stationFragment;
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
