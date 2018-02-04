package com.archidni.archidni.UiUtils;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class TransportMeansSelector {
    private ArrayList<TransportMean> selectedTransportMeans;

    public TransportMeansSelector() {
        selectedTransportMeans = new ArrayList<>();
    }

    public void ToggleTransportMean(int transportMeanId)
    {
        if (isTransportMeanSelected(transportMeanId))
        {
            deselectTransportMean(transportMeanId);
        }
        else
        {
            selectTransportMean(transportMeanId);
        }
    }
    public boolean isTransportMeanSelected (int transportMeanId)
    {
        return selectedTransportMeans.contains(TransportMean.allTransportMeans.get(transportMeanId));
    }
    public boolean allTransportMeansSelected ()
    {
        return (selectedTransportMeans.size()==4);
    }
    private void deselectTransportMean (int transportMeanId)
    {
        selectedTransportMeans.remove(TransportMean.allTransportMeans.get(transportMeanId));
    }
    private void selectTransportMean (int transportMeanId)
    {
        selectedTransportMeans.add(TransportMean.allTransportMeans.get(transportMeanId));
    }
    public void selectAllTransportMeans ()
    {
        selectedTransportMeans = new ArrayList<>();
        for (TransportMean transportMean : TransportMean.allTransportMeans)
        {
            if (!isTransportMeanSelected(transportMean.getId()))
            {
                this.ToggleTransportMean(transportMean.getId());
            }

        }
    }

    private void deselectAllTransportMeans ()
    {
        selectedTransportMeans = new ArrayList<>();
    }

    public ArrayList<TransportMean> getSelectedTransportMeans() {
        return selectedTransportMeans;
    }


    public void setSelectedTransportMeans(ArrayList<TransportMean> selectedTransportMeans) {
        this.selectedTransportMeans = selectedTransportMeans;
    }
}
