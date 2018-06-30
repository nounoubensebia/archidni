package com.archidni.archidni.UiUtils;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class TransportMeansSelector {
    private ArrayList<SelectorItem> selectedItems;

    public TransportMeansSelector() {
        selectedItems = new ArrayList<>();
    }

    public void ToggleItem(int ItemId)
    {
        if (isItemSelected(ItemId))
        {
            deselectItem(ItemId);
        }
        else
        {
            selectTransportMean(ItemId);
        }
    }

    public ArrayList<TransportMean> getSelectedTransportMeans ()
    {
        ArrayList<TransportMean> transportMeans = new ArrayList<>();
        for (SelectorItem selectorItem:selectedItems)
        {
            if (selectorItem instanceof TransportMean)
            {
                transportMeans.add((TransportMean)selectorItem);
            }
        }
        return transportMeans;
    }

    public boolean isItemSelected(int itemId)
    {
        //return selectedItems.contains(TransportMean.allTransportMeans.get(itemId));
        for (SelectorItem selectorItem:selectedItems)
        {
            if (selectorItem.getId()==itemId)
            {
                return true;
            }
        }
        return false;
    }
    public boolean allTransportMeansSelected ()
    {
        return (selectedItems.size()==4);
    }
    private void deselectItem(int transportMeanId)
    {
        selectedItems.remove(SelectorItem.allItems.get(transportMeanId));
    }
    private void selectTransportMean (int transportMeanId)
    {
        selectedItems.add(SelectorItem.allItems.get(transportMeanId));
    }
    public void selectAllItems()
    {
        selectedItems = new ArrayList<>();
        for (SelectorItem item : SelectorItem.allItems)
        {
            if (!isItemSelected(item.getId()))
            {
                this.ToggleItem(item.getId());
            }

        }
    }

    private void deselectAllTransportMeans ()
    {
        selectedItems = new ArrayList<>();
    }

    public ArrayList<SelectorItem> getSelectedItems() {
        return selectedItems;
    }


    public void setSelectedItems(ArrayList<SelectorItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
