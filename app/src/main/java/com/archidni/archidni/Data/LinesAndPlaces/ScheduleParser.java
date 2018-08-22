package com.archidni.archidni.Data.LinesAndPlaces;

import com.archidni.archidni.Data.Station.StationParser;
import com.archidni.archidni.Model.Transport.MetroSchedule;
import com.archidni.archidni.Model.Transport.Schedule;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationTime;
import com.archidni.archidni.Model.Transport.TrainSchedule;
import com.archidni.archidni.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleParser {

    private String json;

    public ScheduleParser(String json) {
        this.json = json;
    }

    public List<Schedule> parseSchedules () throws JSONException
    {
        JSONArray root = new JSONArray(json);
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (int i=0;i<root.length();i++)
        {
            JSONObject jsonObject = root.getJSONObject(i);
            Schedule schedule = parseSchedule(jsonObject);
            schedules.add(schedule);
        }
        return schedules;
    }

    private Schedule parseSchedule (JSONObject jsonObject) throws JSONException
    {
        Schedule schedule = null;
        if (jsonObject.has("waiting_time"))
        {
            int days = jsonObject.getInt("days");
            long startTime = TimeUtils.getTimeFromString(jsonObject.getString("start_time"));
            long endTime = TimeUtils.getTimeFromString(jsonObject.getString("end_time"));
            int waitingTime = jsonObject.getInt("waiting_time");
            schedule = new MetroSchedule(days,startTime,endTime,waitingTime);
        }
        else
        {
            int days = jsonObject.getInt("days");
            JSONArray departuresArray = jsonObject.getJSONArray("departures");
            ArrayList<Long> departures = new ArrayList<>();
            for (int i=0;i<departuresArray.length();i++)
            {
                departures.add(TimeUtils.getTimeFromString(departuresArray.getJSONObject(i).getString("time")));
            }
            JSONArray stationsArray = jsonObject.getJSONArray("stations");
            ArrayList<StationTime> stationTimes = new ArrayList<>();
            for (int i=0;i<stationsArray.length();i++)
            {
                JSONObject stationJSONObject = stationsArray.getJSONObject(i);
                Station station = new StationParser().parseStation(stationJSONObject);
                int timeAtStation = stationJSONObject.getJSONObject("pivot").getInt("minutes");
                StationTime stationTime = new StationTime(station,timeAtStation);
                stationTimes.add(stationTime);
            }
            schedule = new TrainSchedule(days,departures,stationTimes);
        }
        return schedule;
    }
}
