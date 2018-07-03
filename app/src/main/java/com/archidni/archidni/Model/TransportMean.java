package com.archidni.archidni.Model;

import android.graphics.drawable.Drawable;

import com.archidni.archidni.App;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.SelectorItem;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by noure on 02/02/2018.
 */

public class TransportMean extends SelectorItem {
    public static final int ID_TRAMWAY = 3;
    public static final int ID_METRO = 0;
    public static final int ID_BUS = 2;
    public static final int ID_TELEPHERIQUE = 4;
    public static final int ID_TRAIN = 1;


    public static ArrayList<TransportMean> allTransportMeans =
            new ArrayList<TransportMean>(){{
                add(new TransportMean(0, App.getAppContext().getString(R.string.metro)));
                add(new TransportMean(1, App.getAppContext().getString(R.string.train)));
                add(new TransportMean(2, App.getAppContext().getString(R.string.bus)));
                add(new TransportMean(3, App.getAppContext().getString(R.string.tramway)));
                add(new TransportMean(4,"Téléphérique"));
            }};

    public TransportMean(int id, String name) {
        super(id, name);
    }

    @Override
    public int getIconDisabled ()
    {
        switch (getId()){
            case 0:return R.drawable.ic_transport_mean_0_disabled;
            case 1:return R.drawable.ic_transport_mean_1_disabled;
            case 2:return R.drawable.ic_transport_mean_2_disabled;
            case 3:return R.drawable.ic_transport_mean_3_disabled;
            case 4:return R.drawable.ic_transport_mean_4_disabled;
            default:return -1;
        }
    }

    @Override
    public int getIconEnabled ()
    {
        switch (getId()){
            case 0:return R.drawable.ic_transport_mean_0_enabled;
            case 1:return R.drawable.ic_transport_mean_1_enabled;
            case 2:return R.drawable.ic_transport_mean_2_enabled;
            case 3:return R.drawable.ic_transport_mean_3_enabled;
            case 4:return R.drawable.ic_transport_mean_4_enabled;
            default:return -1;
        }
    }

    @Override
    public int getMarkerIcon()
    {
        switch (getId()){
            case 0 : //return R.drawable.marker_transport_mean_0;
                return R.drawable.marker_transport_0;
            case 1 :
                //return R.drawable.marker_transport_mean_1;
                return R.drawable.marker_transport_1;
            case 2 : //return R.drawable.marker_transport_mean_2;
                return R.drawable.marker_transport_2;
            case 3 : //return R.drawable.marker_transport_mean_3;
                return R.drawable.marker_transport_3;
            case 4 : //return R.drawable.marker_transport_mean_4;
                return R.drawable.marker_transport_3;
            default:return -1;
        }
    }

    public int getFabIcon()
    {
        switch (getId()){
            case 0 : return R.drawable.ic_fab_transport_mean_0;
            case 1 : return R.drawable.ic_fab_transport_mean_1;
            case 2 : return R.drawable.ic_fab_transport_mean_2;
            case 3 : return R.drawable.ic_fab_transport_mean_3;
            case 4:  return R.drawable.ic_fab_transport_mean_2;
            default:return -1;
        }
    }

    @Override
    public int getColor()
    {
        switch (getId()){
            case 0 : return R.color.color_transport_mean_selected_0;
            case 1 : return R.color.color_transport_mean_selected_1;
            case 2 : return R.color.color_transport_mean_selected_2;
            case 3 : return R.color.color_transport_mean_selected_3;
            case 4:  return R.color.color_transport_mean_selected_4;
            default:return -1;
        }
    }


    @Override
    public int getTheme()
    {
        switch (getId()){
            case 0 : return R.style.TransportMeanTheme0;
            case 1 : return R.style.TransportMeanTheme1;
            case 2 : return R.style.TransportMeanTheme2;
            case 3 : return R.style.TransportMeanTheme3;
            case 4 : return R.style.TransportMeanTheme4;
            default:return -1;
        }
    }

    public int getCircleDrawable ()
    {
        switch (getId()){
            case 0 : return R.drawable.ic_circle_transport_mean_0;
            case 1 : return R.drawable.ic_circle_transport_mean_1;
            case 2 : return R.drawable.ic_circle_transport_mean_2;
            case 3 : return R.drawable.ic_circle_transport_mean_3;
            case 4 : return R.drawable.ic_circle_transport_mean_4;
            default:return -1;
        }
    }

    @Override
    public int getCoordinateDrawable ()
    {
        switch (getId())
        {
            case 0 : return R.drawable.ic_coordinate_transport_mean_0;
            case 1 : return R.drawable.ic_coordinate_transport_mean_1;
            case 2 : return R.drawable.ic_coordinate_transport_mean_2;
            case 3 : return R.drawable.ic_coordinate_transport_mean_3;
            case 4 : return R.drawable.ic_coordinate_transport_mean_4;
            default:return -1;
        }
    }

    public int getLinesSelectedDrawable ()
    {
        switch (getId())
        {
            case 0 : return R.drawable.ic_line_enabled_transport_mean_0;
            case 1 : return R.drawable.ic_line_enabled_transport_mean_1;
            case 2 : return R.drawable.ic_line_enabled_transport_mean_2;
            case 3 : return R.drawable.ic_line_enabled_transport_mean_3;
            case 4 : return R.drawable.ic_line_enabled_transport_mean_4;
            default:return -1;
        }
    }

    public int getTimesSelectedDrawable ()
    {
        switch (getId())
        {
            case 0 : return R.drawable.ic_time_transport_mean_0;
            case 1 : return R.drawable.ic_time_transport_mean_1;
            case 2 : return R.drawable.ic_time_transport_mean_2;
            case 3 : return R.drawable.ic_time_transport_mean_3;
            case 4 : return R.drawable.ic_time_transport_mean_4;
            default:return -1;
        }
    }


    public int getStationCirleDrawableId() {
        switch (getId())
        {
            case 0 : return R.drawable.selector_circle_button_transport_mean0;
            case 1 : return R.drawable.selector_circle_button_transport_mean1;
            case 2 : return R.drawable.selector_circle_button_transport_mean2;
            case 3 : return R.drawable.selector_circle_button_transport_mean3;
            case 4 : return R.drawable.selector_circle_button_transport_mean4;
            default:return -1;
        }
    }

    public int getSeparationViewId() {
        switch (getId())
        {
            case 0 : return R.id.view_separation_transport_mean1;
            case 1 : return R.id.view_separation_transport_mean2;
            case 2 : return R.id.view_separation_transport_mean3;
            case 3 : return R.id.view_separation_transport_mean4;
            case 4 : return R.id.view_separation_transport_mean5;
            default:return -1;
        }
    }

    public int getSeparationView2Id() {
        switch (getId())
        {
            case 0 : return R.id.view_separation1_transport_mean1;
            case 1 : return R.id.view_separation1_transport_mean2;
            case 2 : return R.id.view_separation1_transport_mean3;
            case 3 : return R.id.view_separation1_transport_mean4;
            case 4 : return R.id.view_separation_transport_mean5;
            default:return -1;
        }
    }

    public int getExitDrawable() {
        switch (getId())
        {
            case 0 : return R.drawable.ic_exit_transport_mean0;
            case 1 : return R.drawable.ic_exit_transport_mean1;
            case 2 : return R.drawable.ic_exit_transport_mean2;
            case 3 : return R.drawable.ic_exit_transport_mean3;
            case 4 : return R.drawable.ic_exit_transport_mean4;
            default:return -1;
        }
    }

    public int getCircleFullDrawableId() {
        switch (getId())
        {
            case 0 : return R.drawable.shape_button_circle_full_transport_mean0;
            case 1 : return R.drawable.shape_button_circle_full_transport_mean1;
            case 2 : return R.drawable.shape_button_circle_full_transport_mean2;
            case 3 : return R.drawable.shape_button_circle_full_transport_mean3;
            case 4 : return R.drawable.shape_button_circle_full_transport_mean4;
            default:return -1;
        }
    }

    public int getWhiteIconDrawableId() {
        switch (getId())
        {
            case 0 : return R.drawable.ic_transport_mean_0_white;
            case 1 : return R.drawable.ic_transport_mean_1_white;
            case 2 : return R.drawable.ic_transport_mean_2_white;
            case 3 : return R.drawable.ic_transport_mean_3_white;
            case 4 : return R.drawable.ic_transport_mean_4_white;
            default:return -1;
        }
    }

    public IconGenerator getClusterIconGenerator()
    {
        Drawable clusterIcon = getApplicationContext().getResources().getDrawable(R.drawable.ic_lens_black_24dp);
        //clusterIcon.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
        IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        mClusterIconGenerator.setBackground(clusterIcon);
        return mClusterIconGenerator;
    }
}
