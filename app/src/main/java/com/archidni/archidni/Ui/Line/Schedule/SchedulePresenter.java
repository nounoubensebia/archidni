package com.archidni.archidni.Ui.Line.Schedule;

import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.Schedule;

import java.util.List;

public class SchedulePresenter implements ScheduleContract.Presenter {

    private ScheduleContract.View view;
    private Line line;
    private List<Schedule> schedules;
    private LinesAndPlacesRepository linesAndPlacesRepository;

    public SchedulePresenter(ScheduleContract.View view, Line line) {
        this.view = view;
        this.line = line;
        view.setTheme(line.getTransportMean().getTheme());
    }


    @Override
    public void onCreate() {
        linesAndPlacesRepository = new LinesAndPlacesRepository();
        linesAndPlacesRepository.getSchedules(line, new LinesAndPlacesRepository.OnSchedulesSearchCompleted() {
            @Override
            public void onSchedulesFound(List<Schedule> schedules) {
                SchedulePresenter.this.schedules = schedules;
                view.showSchedulesLayout();
                view.showSchedules(line,schedules);
            }

            @Override
            public void onError() {
                view.showSchedulesLayout();
                view.showErrorMessage();
            }
        });
    }

    @Override
    public void onFragmentCreated() {
        if (schedules!=null)
        {
            view.showSchedulesLayout();
            view.showSchedules(line,schedules);
        }
    }
}
