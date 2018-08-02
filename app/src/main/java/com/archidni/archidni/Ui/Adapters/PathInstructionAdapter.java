package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.archidni.archidni.Model.Path.WaitLine;
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
            populateWalkInstruction((WalkInstruction)pathInstruction,item);
        }

        if (pathInstruction instanceof WaitInstruction)

        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_wait_instruction
                    ,parent, false);
            populateWaitInstruction((WaitInstruction)pathInstruction,item);
        }

        return item;
    }

    private void populateWalkInstruction (WalkInstruction walkInstruction,View view)
    {
        TextView typeText = view.findViewById(R.id.text_type);
        TextView instructionText = view.findViewById(R.id.text_instruction);
        TextView durationText = view.findViewById(R.id.text_time);
        TextView distanceText = view.findViewById(R.id.text_distance);

        typeText.setText(walkInstruction.getTtile());
        instructionText.setText(walkInstruction.getMainText());
        durationText.setText(walkInstruction.getDuration()/60 + " minutes");
        distanceText.setText(walkInstruction.getDistanceString());
    }

    private void populateWaitInstruction (WaitInstruction waitInstruction,View view)
    {
        TextView titleText = view.findViewById(R.id.text_title);
        TextView takeLinesText = view.findViewById(R.id.text_take_lines);
        RecyclerView linesList = view.findViewById(R.id.recyclerView);
        titleText.setText(waitInstruction.getTtile());
        takeLinesText.setText(waitInstruction.getTakeLineText());
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LineInsideWaitInstructionAdapter lineInsideWaitInstructionAdapter = new
                LineInsideWaitInstructionAdapter(getContext(),
                waitInstruction.getWaitLines(), new LineInsideWaitInstructionAdapter.OnItemClick() {
            @Override
            public void onItemClick(WaitLine waitLine) {

            }
        }
        );
        linesList.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        linesList.setItemAnimator(new DefaultItemAnimator());
        linesList.setAdapter(lineInsideWaitInstructionAdapter);
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