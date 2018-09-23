package com.archidni.archidni.Model.Interests;

import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.SelectorItem;

public class HospitalItem extends SelectorItem {

    public HospitalItem(int id,String name)
    {
        super(id,name);
    }

    @Override
    public int getIconDisabled() {
        return R.drawable.ic_healing_disabled_24dp;
    }

    @Override
    public int getMarkerIcon() {
        return 0;
    }

    @Override
    public int getIconEnabled() {
        return R.drawable.ic_healing_enabled_24dp;
    }

    @Override
    public int getColor() {
        return R.color.colorRed;
    }

    @Override
    public int getTheme() {
        return R.style.TransportMeanTheme0;
    }

    @Override
    public int getCoordinateDrawable() {
        return R.drawable.ic_coordinate_transport_mean_0;
    }
}
