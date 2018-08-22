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

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule;
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


    private int day;
    private boolean isReady = false;
    private OnFragmentReady onFragmentReady;

    public DayScheduleFragment(int day, OnFragmentReady onFragmentReady) {
        this.day = day;
        this.onFragmentReady = onFragmentReady;
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
            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getContext(),schedules,line);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL
                    ,false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(scheduleAdapter);
        }
    }

    public void showListLayout ()
    {
        if (isReady)
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    public void hideListLayout ()
    {
        if (isReady)
        {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnFragmentReady {
        public void onReady();
    }

}
