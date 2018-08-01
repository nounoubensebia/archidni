package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 11/02/2018.
 */

public class PathInstructionAdapter extends ArrayAdapter<PathInstruction> {
    private ArrayList<PathInstruction> pathInstructions;
    private int selectedItem;
    private OnPolylineSelected onPolylineSelected;
    private OnCoordinateSelected onCoordinateSelected;
    public PathInstructionAdapter(@NonNull Context context, ArrayList<PathInstruction> pathInstructions) {
        super(context, 0,pathInstructions);
        this.pathInstructions = pathInstructions;
        selectedItem = -1;
    }

    public void setOnPolylineSelected(OnPolylineSelected onPolylineSelected) {
        this.onPolylineSelected = onPolylineSelected;
    }

    public void setOnCoordinateSelected(OnCoordinateSelected onCoordinateSelected) {
        this.onCoordinateSelected = onCoordinateSelected;
    }

    public void selectElement (int element)
    {
        if (selectedItem == element)
        {
            selectedItem = -1;
            notifyDataSetChanged();
        }
        else
        {
            selectedItem = element;
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PathInstruction pathInstruction = getItem(position);
        View item = convertView;

        /*if (pathInstruction instanceof RideInstruction ||position==pathInstructions.size()-1)
        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_path_instruction_transport ,parent, false);
        }
        else
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_path_instruction ,parent, false);*/
        if (pathInstruction instanceof RideInstruction)
        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_ride_instruction
                    ,parent, false);
        }

        if (pathInstruction instanceof WalkInstruction)

        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_walk_instruction
                    ,parent, false);
        }

        if (pathInstruction instanceof WaitInstruction)

        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_wait_instruction
                    ,parent, false);
        }

        return item;
    }

    private void inflateRideInstructionView (RideInstruction rideInstruction,View view)
    {
        //TextView titleText = view.findViewById(R.id.text_line_name);

    }

    public interface OnPolylineSelected {
        public void onPolylineSelected (ArrayList<Coordinate> polyline, int selectedItem);
    }
    public interface OnCoordinateSelected {
        public void onCoordinateSelected (Coordinate coordinate,int selectedItem);
    }
}