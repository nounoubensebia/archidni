package com.archidni.archidni.Model.Transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScheduleRetriever {
    private List<Schedule> schedules;

    public ScheduleRetriever(List<Schedule> schedules) {
        this.schedules = schedules;
    }


    /**
     *
      * @param day day code
     *  day codes can be found in TimeUtils class
     * @return a list of schedules
     */
    public List<Schedule> getDaySchedules (long day)
    {
        List<Schedule> daySchedules = new ArrayList<>();
        for (Schedule schedule:schedules)
        {
            if ((schedule.getDays()&day)!=0)
            {
                daySchedules.add(schedule);
            }
        }
        Collections.sort(daySchedules, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule, Schedule t1) {
                if (schedule instanceof MetroSchedule)
                    return (int)(((MetroSchedule) schedule).getStartTime()
                            - ((MetroSchedule)t1).getStartTime());
                else
                    return 0;
            }
        });
        return daySchedules;
    }
}
