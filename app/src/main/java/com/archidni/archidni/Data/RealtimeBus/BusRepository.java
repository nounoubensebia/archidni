package com.archidni.archidni.Data.RealtimeBus;

import com.archidni.archidni.Model.RealtimeBus.Bus;

import java.util.List;

public interface BusRepository {

    public void getBuses (OnBusesFound onBusesFound);



    public interface OnBusesFound {
        void onBusesFound(List<Bus> buses);
        void onError();
    }

}
