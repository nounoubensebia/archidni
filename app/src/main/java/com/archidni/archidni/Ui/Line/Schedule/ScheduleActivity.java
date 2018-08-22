package com.archidni.archidni.Ui.Line.Schedule;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule;
import com.archidni.archidni.Model.Transport.ScheduleRetriever;
import com.archidni.archidni.R;
import com.archidni.archidni.TimeUtils;
import com.archidni.archidni.Ui.SearchLineStation.LineFragment;
import com.archidni.archidni.Ui.SearchLineStation.SearchLineStationActivity;
import com.archidni.archidni.Ui.SearchLineStation.StationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleActivity extends AppCompatActivity implements ScheduleContract.View {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private boolean onPresneterCreateCalled = false;

    PageAdapter pageAdapter;

    ScheduleContract.Presenter presenter;

    ArrayList<DayScheduleFragment> dayScheduleFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        Line line = Line.fromJson(extras.getString(IntentUtils.LINE_LINE));
        presenter = new SchedulePresenter(this,line);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        dayScheduleFragments = new ArrayList<>();
        initViews();
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, line.getTransportMean().getColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(4);
        }
    }

    private void initViews ()
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        presenter.onCreate();
    }

    @Override
    public void showSchedules(Line line,List<Schedule> schedules) {
        ScheduleRetriever scheduleRetriever = new ScheduleRetriever(schedules);
        for (DayScheduleFragment dayScheduleFragment:dayScheduleFragments)
        {
            long day = TimeUtils.getDayFromStandardOrder(dayScheduleFragment.getDay());
            if (dayScheduleFragment.isReady())
            {
                dayScheduleFragment.showListLayout();
                dayScheduleFragment.populateRecyclerView(line,scheduleRetriever.getDaySchedules(day));
            }
        }
    }

    @Override
    public void showSchedulesLayout() {
        for (DayScheduleFragment dayScheduleFragment:dayScheduleFragments)
        {
            if (dayScheduleFragment.isReady())
                dayScheduleFragment.showListLayout();
        }
    }

    @Override
    public void hideSchedulesLayout() {
        for (DayScheduleFragment dayScheduleFragment:dayScheduleFragments)
        {
            if (dayScheduleFragment.isReady())
            dayScheduleFragment.hideListLayout();
        }
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,R.string.error_happened,Toast.LENGTH_LONG).show();
    }


    public class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DayScheduleFragment dayScheduleFragment =  new DayScheduleFragment(position,
                    new DayScheduleFragment.OnFragmentReady() {
                        @Override
                        public void onReady() {
                            presenter.onFragmentCreated();
                        }
                    });
            dayScheduleFragments.add(dayScheduleFragment);
            return dayScheduleFragment;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:return "Dim";
                case 1:return "Lun";
                case 2:return "Mar";
                case 3:return "Mer";
                case 4:return "Jeu";
                case 5:return "Ven";
                case 6:return "Sam";
            }
            return "";
        }
    }
}
