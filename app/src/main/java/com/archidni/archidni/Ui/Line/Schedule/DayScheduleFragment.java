package com.archidni.archidni.Ui.Line.Schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.Schedule;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.ScheduleAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class DayScheduleFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.text_schedule_list)
    TextView schedulesListText;

    private int day;
    private boolean isReady = false;
    private OnFragmentReady onFragmentReady;
    private OnScheduleClick onScheduleClick;

    public DayScheduleFragment(int day, OnFragmentReady onFragmentReady, OnScheduleClick onScheduleClick) {
        super();
        this.day = day;
        this.onFragmentReady = onFragmentReady;
        this.onScheduleClick = onScheduleClick;
    }

    public boolean isReady() {
        return isReady;
    }

    public int getDay() {
        return day;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_day_schedule, container, false);
        ButterKnife.bind(this,rootView);
        isReady = true;
        onFragmentReady.onReady();
        return rootView;
    }

    public void populateRecyclerView (Line line, List<Schedule> schedules)
    {
        if (isReady)
        {
            if (schedules.size()>0)
            {
                schedulesListText.setVisibility(View.VISIBLE);
                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getContext(), schedules, line, new ScheduleAdapter.OnTrainScheduleClick() {
                    @Override
                    public void onItemClick(TrainSchedule trainSchedule) {
                        onScheduleClick.onScheduleClick(trainSchedule);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL
                        ,false));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(scheduleAdapter);
                if (schedules.size()>0)
                if (schedules.get(0) instanceof TrainSchedule)
                {
                    schedulesListText.setText("Liste des voyages : ");
                }
                else
                {
                    schedulesListText.setText("Horraires : ");
                }
            }
            else
            {
                schedulesListText.setVisibility(View.GONE);
            }
        }
    }

    public void showListLayout ()
    {
        if (isReady)
        {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    public void hideListLayout ()
    {
        if (isReady)
        {
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
    }

    public interface OnFragmentReady {
        public void onReady();
    }

    public interface OnScheduleClick {
        public void onScheduleClick(Schedule schedule);
    }

}
