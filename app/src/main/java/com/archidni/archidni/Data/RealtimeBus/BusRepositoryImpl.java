package com.archidni.archidni.Data.RealtimeBus;

public class BusRepositoryImpl implements BusRepository {

    @Override
    public void getBuses(OnBusesFound onBusesFound) {
        BusDataStore busDataStore = new BusDataStoreImpl();
        busDataStore.getBuses(onBusesFound);
    }
}
