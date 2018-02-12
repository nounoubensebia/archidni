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

        if (pathInstruction instanceof RideInstruction ||position==pathInstructions.size()-1)
        {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_path_instruction_transport ,parent, false);
        }
        else
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_path_instruction ,parent, false);


        if (pathInstruction instanceof RideInstruction ||position==pathInstructions.size()-1)
        {
            ImageView imageInstruction1 = (ImageView) item.findViewById(R.id.image_instruction1);
            ImageView imageInstruction2 = (ImageView) item.findViewById(R.id.image_instruction2);
            TextView mainInstructionText = (TextView)item.findViewById(R.id.text_instruction_label);
            TextView mainInstructionText2 = (TextView)item.findViewById(R.id.text_instruction2);
            ImageView circle1Image = (ImageView)item.findViewById(R.id.image_circle1);
            ImageView circle2Image = (ImageView)item.findViewById(R.id.image_circle2);
            TextView secondaryText = (TextView) item.findViewById(R.id.text_instruction_details);
            View instruction1TextLayout = item.findViewById(R.id.layout_text_instruction);
            View instruction2TextLayout = item.findViewById(R.id.layout_text_instruction2);
            View instruction1Layout = item.findViewById(R.id.layout_instruction1);
            View instruction2Layout = item.findViewById(R.id.layout_instruction2);
            if (pathInstruction instanceof RideInstruction)
            {

                final RideInstruction rideInstruction = (RideInstruction) pathInstruction;
                View separationView2 = item.findViewById(rideInstruction.getTransportMean().getSeparationView2Id());
                View separationView = item.findViewById(rideInstruction.getTransportMean().getSeparationViewId());
                if (position!=getCount()-1)
                    separationView2.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(getContext(),(rideInstruction.getTransportMean().getIconEnabled()));
                imageInstruction1.setImageDrawable(drawable);
                drawable = ContextCompat.getDrawable(getContext(),rideInstruction.getTransportMean().getExitDrawable());
                imageInstruction2.setImageDrawable(drawable);
                separationView.setVisibility(View.VISIBLE);
                mainInstructionText.setText(rideInstruction.getMainText());
                mainInstructionText2.setText(rideInstruction.getExitInstructionText());
                drawable = ContextCompat.getDrawable(getContext(),rideInstruction.getTransportMean().getStationCirleDrawableId());
                if (position*10==selectedItem)
                {
                    Drawable drawable1 = ContextCompat.getDrawable(getContext(),rideInstruction.getTransportMean().getCircleFullDrawableId());
                    circle1Image.setImageDrawable(drawable1);
                    drawable1 = ContextCompat.getDrawable(getContext(),rideInstruction.getTransportMean().getWhiteIconDrawableId());
                    imageInstruction1.setImageDrawable(drawable1);
                }
                else
                {
                    circle1Image.setImageDrawable(drawable);
                }
                if ((position*10+1)==selectedItem)
                {
                    Drawable drawable1 = ContextCompat.getDrawable(getContext(),rideInstruction.getTransportMean().getCircleFullDrawableId());
                    circle2Image.setImageDrawable(drawable1);
                    drawable1 = ContextCompat.getDrawable(getContext(),R.drawable.ic_exit_white);
                    imageInstruction2.setImageDrawable(drawable1);
                }
                else
                {
                    circle2Image.setImageDrawable(drawable);
                }
                secondaryText.setText(pathInstruction.getSecondaryText());
                instruction1TextLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(rideInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(rideInstruction.getPolyline(),-1);
                        }
                    }
                });
                instruction1Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(rideInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(rideInstruction.getPolyline(),-1);
                        }
                    }
                });
                instruction2TextLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem != position*10+1)
                        {
                            onCoordinateSelected.onCoordinateSelected(rideInstruction.getPolyline().
                                            get(rideInstruction.getPolyline().size()-1),
                                    position*10+1);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(rideInstruction.getPolyline().
                                            get(rideInstruction.getPolyline().size()-1),
                                    -1);
                        }
                    }
                });
                instruction2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem != position*10+1)
                        {
                            onCoordinateSelected.onCoordinateSelected(rideInstruction.getPolyline().
                                            get(rideInstruction.getPolyline().size()-1),
                                    position*10+1);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(rideInstruction.getPolyline().
                                            get(rideInstruction.getPolyline().size()-1),
                                    -1);
                        }
                    }
                });
            }
            else
            {
                View separationView1 = item.findViewById(R.id.view_separation_transport_mean3);
                separationView1.setVisibility(View.VISIBLE);
                Drawable drawable1;
                Drawable drawable;
                if (selectedItem!=position*10+1)
                {
                    drawable1 = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_empty_transport_mean2);
                    drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_marker_green_24dp);
                }
                else
                {
                    drawable1 = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_full_transport_mean2);
                    drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_marker_white_24dp);
                }
                circle2Image.setImageDrawable(drawable1);
                imageInstruction2.setImageDrawable(drawable);
                mainInstructionText.setText(pathInstruction.getMainText());
                secondaryText.setText(pathInstruction.getSecondaryText());
                mainInstructionText2.setText("Destination atteinte !");
                if (selectedItem!=position*10)
                {
                    drawable1 = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_empty_transport_mean2);
                    drawable = ContextCompat.getDrawable(getContext(), (int) pathInstruction.getInstructionIcon());
                }
                else
                {
                    drawable1 = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_full_transport_mean2);
                    drawable = ContextCompat.getDrawable(getContext(), (int) pathInstruction.getInstructionWhiteIcon());
                }
                imageInstruction1.setImageDrawable(drawable);
                circle1Image.setImageDrawable(drawable1);
                instruction1TextLayout.setOnClickListener(new View.OnClickListener() {
                    WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                    @Override
                    public void onClick(View v) {
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),-1);
                        }
                    }
                });
                instruction1Layout.setOnClickListener(new View.OnClickListener() {
                    WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                    @Override
                    public void onClick(View v) {
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),-1);
                        }
                    }
                });
                instruction2TextLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                        if (selectedItem != position*10+1)
                        {
                            onCoordinateSelected.onCoordinateSelected(walkInstruction.getPolyline().
                                            get(walkInstruction.getPolyline().size()-1),
                                    position*10+1);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(walkInstruction.getPolyline().
                                            get(walkInstruction.getPolyline().size()-1),
                                    -1);
                        }
                    }
                });
                instruction2Layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                        if (selectedItem != position*10+1)
                        {
                            onCoordinateSelected.onCoordinateSelected(walkInstruction.getPolyline().
                                            get(walkInstruction.getPolyline().size()-1),
                                    position*10+1);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(walkInstruction.getPolyline().
                                            get(walkInstruction.getPolyline().size()-1),
                                    -1);
                        }
                    }
                });
            }
        }
        else
        {
            View textInstructionLayout = item.findViewById(R.id.layout_text_instruction);
            TextView instructionText = (TextView) item.findViewById(R.id.text_instruction_label);
            TextView secondaryText = (TextView) item.findViewById(R.id.text_instruction_details);
            View instructionLayout = item.findViewById(R.id.layout_instruction1);
            instructionText.setText(pathInstruction.getMainText());
            secondaryText.setText(pathInstruction.getSecondaryText());
            ImageView imageView = (ImageView) item.findViewById(R.id.image_instruction1);
            ImageView circleImage = (ImageView) item.findViewById(R.id.circle_instruction1);
            if (selectedItem!=position*10)
            {
                Drawable drawable = ContextCompat.getDrawable(getContext(),(int)pathInstruction.getInstructionIcon());
                imageView.setImageDrawable(drawable);
                drawable = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_empty_transport_mean2);
                circleImage.setImageDrawable(drawable);
            }
            else
            {
                Drawable drawable = ContextCompat.getDrawable(getContext(),(int)pathInstruction.getInstructionWhiteIcon());
                imageView.setImageDrawable(drawable);
                drawable = ContextCompat.getDrawable(getContext(),R.drawable.shape_button_circle_full_transport_mean2);
                circleImage.setImageDrawable(drawable);
            }
            View separationView = item.findViewById(R.id.view_separation_transport_mean3);
            separationView.setVisibility(View.VISIBLE);

            instructionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pathInstruction instanceof WaitInstruction)
                    {
                        WaitInstruction waitInstruction = (WaitInstruction) pathInstruction;
                        if (selectedItem!=position*10)
                        {
                            onCoordinateSelected.onCoordinateSelected(waitInstruction.getCoordinate(),position*10);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(waitInstruction.getCoordinate(),-1);
                        }
                    }
                    else
                    {
                        WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),-1);
                        }
                    }
                }
            });

            textInstructionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pathInstruction instanceof WaitInstruction)
                    {
                        WaitInstruction waitInstruction = (WaitInstruction) pathInstruction;
                        if (selectedItem!=position*10)
                        {
                            onCoordinateSelected.onCoordinateSelected(waitInstruction.getCoordinate(),position*10);
                        }
                        else
                        {
                            onCoordinateSelected.onCoordinateSelected(waitInstruction.getCoordinate(),-1);
                        }
                    }
                    else
                    {
                        WalkInstruction walkInstruction = (WalkInstruction) pathInstruction;
                        if (selectedItem!=position*10)
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),position*10);
                        }
                        else
                        {
                            onPolylineSelected.onPolylineSelected(walkInstruction.getPolyline(),-1);
                        }
                    }
                }
            });
        }

        return item;
    }

    public interface OnPolylineSelected {
        public void onPolylineSelected (ArrayList<Coordinate> polyline, int selectedItem);
    }
    public interface OnCoordinateSelected {
        public void onCoordinateSelected (Coordinate coordinate,int selectedItem);
    }
}