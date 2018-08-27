package com.archidni.archidni.Ui.Line.Schedule;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Schedule.Schedule;
import com.archidni.archidni.Model.Transport.Schedule.TrainSchedule;

import java.util.List;

public interface ScheduleContract {
    public interface View {
        public void showSchedules (Line line, List<Schedule> schedules);
        public void showSchedulesLayout ();
        public void hideSchedulesLayout();
        public void showErrorMessage();
        public void setTheme (int themeId);
        public void startTrainTripActivity(TrainSchedule trainSchedule);
    }

    public interface Presenter {
        public void onCreate();
        public void onFragmentCreated();
        public void onScheduleClick(Schedule schedule);
    }
}
