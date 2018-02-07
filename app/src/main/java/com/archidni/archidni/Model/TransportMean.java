package com.archidni.archidni.Model;

import com.archidni.archidni.App;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class TransportMean {
    private int id;
    private String name;
    public static ArrayList<TransportMean> allTransportMeans =
            new ArrayList<TransportMean>(){{
                add(new TransportMean(0, App.getAppContext().getString(R.string.metro)));
                add(new TransportMean(1, App.getAppContext().getString(R.string.train)));
                add(new TransportMean(2, App.getAppContext().getString(R.string.bus)));
                add(new TransportMean(3, App.getAppContext().getString(R.string.tramway)));
            }};

    public TransportMean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getIconDisabled ()
    {
        switch (id){
            case 0:return R.drawable.ic_transport_mean_0_disabled;
            case 1:return R.drawable.ic_transport_mean_1_disabled;
            case 2:return R.drawable.ic_transport_mean_2_disabled;
            case 3:return R.drawable.ic_transport_mean_3_disabled;
            default:return -1;
        }
    }

    public int getIconEnabled ()
    {
        switch (id){
            case 0:return R.drawable.ic_transport_mean_0_enabled;
            case 1:return R.drawable.ic_transport_mean_1_enabled;
            case 2:return R.drawable.ic_transport_mean_2_enabled;
            case 3:return R.drawable.ic_transport_mean_3_enabled;
            default:return -1;
        }
    }

    public String getName() {
        return name;
    }
}
