package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PathInstructionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<PathInstruction> pathInstructions;

    private ArrayList<Boolean> expandedStations;

    private int selectedPosition;

    private static final int TYPE_WALK = 0;
    private static final int TYPE_WAIT = 1;
    private static final int TYPE_RIDE = 2;

    private LineInsideWaitInstructionAdapter.OnItemClick onLineClick;

    private StationInsideRideInstructionAdapter.OnStationClickListener onStationClick;

    private OnItemSelected onItemSelected;

    public PathInstructionRecyclerAdapter(Context context,
                                          ArrayList<PathInstruction> pathInstructions,
                                          LineInsideWaitInstructionAdapter.OnItemClick onLineClick,
                                          StationInsideRideInstructionAdapter.OnStationClickListener onStationClickListener,
                                          OnItemSelected onItemSelected) {
        this.context = context;
        this.pathInstructions = pathInstructions;
        this.onLineClick = onLineClick;
        expandedStations = new ArrayList<>();
        for (PathInstruction pathInstruction:pathInstructions)
        {
            expandedStations.add(false);
        }
        this.onStationClick = onStationClickListener;
        this.onItemSelected = onItemSelected;
        selectedPosition = -1;
    }

    @Override
    public int getItemViewType(int position) {
        PathInstruction pathInstruction = pathInstructions.get(position);
        if (pathInstruction instanceof WalkInstruction)
        {
            return TYPE_WALK;
        }
        if (pathInstruction instanceof WaitInstruction)
        {
            return TYPE_WAIT;
        }
        if (pathInstruction instanceof RideInstruction)
        {
            return TYPE_RIDE;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType)
        {
            case TYPE_WALK : v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_walk_instruction,
                    parent, false);
                    return new WalkInstructionViewHolder(v);
            case TYPE_WAIT : v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_wait_instruction,
                            parent, false);
                return new WaitInstructionViewHolder(v);
            case TYPE_RIDE : v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ride_instruction,
                            parent, false);
                    return new RideInstructionViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PathInstruction pathInstruction = pathInstructions.get(position);
        switch (holder.getItemViewType())
        {
            case TYPE_WALK :
                WalkInstructionViewHolder walkInstructionViewHolder = (WalkInstructionViewHolder)
                        holder;
                populateWalkInstruction((WalkInstruction)pathInstruction,walkInstructionViewHolder,position);
                break;
            case TYPE_WAIT :
                WaitInstructionViewHolder waitInstructionViewHolder = (WaitInstructionViewHolder)
                        holder;
                populateWaitInstruction((WaitInstruction)pathInstruction,waitInstructionViewHolder
                        ,position);
                break;
            case TYPE_RIDE :
                RideInstructionViewHolder rideInstructionViewHolder = (RideInstructionViewHolder)
                        holder;
                populateRideInstruction((RideInstruction)pathInstruction,rideInstructionViewHolder,position);
        }
    }

    private void populateWalkInstruction (final WalkInstruction walkInstruction
            , WalkInstructionViewHolder walkInstructionViewHolder, final int position)
    {
        walkInstructionViewHolder.typeText.setText(walkInstruction.getTtile());
        walkInstructionViewHolder.instructionText.setText(walkInstruction.getMainText());
        walkInstructionViewHolder.durationText.setText(walkInstruction.getDuration()/60 + " minutes");
        walkInstructionViewHolder.distanceText.setText(walkInstruction.getDistanceString());
        walkInstructionViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemSelected.onItemSelected(walkInstruction,position);
            }
        });
    }

    private void populateWaitInstruction (final WaitInstruction waitInstruction,
                                          WaitInstructionViewHolder holder, final int position)
    {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemSelected.onItemSelected(waitInstruction,position);
            }
        });
        holder.titleText.setText(waitInstruction.getTtile());
        holder.takeLinesText.setText(waitInstruction.getTakeLineText());
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LineInsideWaitInstructionAdapter lineInsideWaitInstructionAdapter =
                new LineInsideWaitInstructionAdapter(context,
                waitInstruction.getWaitLines(), new LineInsideWaitInstructionAdapter.OnItemClick() {
            @Override
            public void onItemClick(WaitLine waitLine) {
                onLineClick.onItemClick(waitLine);
            }
        }
        );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.linesList.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        //holder.linesList.setLayoutManager(mLayoutManager);
        holder.linesList.setItemAnimator(new DefaultItemAnimator());
        holder.linesList.setAdapter(lineInsideWaitInstructionAdapter);
        holder.waitIcon.setImageDrawable(ContextCompat.getDrawable(context
                ,waitInstruction.getWaitLines().get(0).getLine().getTransportMean().getTimesSelectedDrawable()));
    }

    private void populateRideInstruction (final RideInstruction rideInstruction,
                                          final RideInstructionViewHolder holder, final int position)
    {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemSelected.onItemSelected(rideInstruction,position);
            }
        });
        holder.transportModeIcon.setImageDrawable(ContextCompat.getDrawable(context,
                rideInstruction.getTransportMean().getIconEnabled()));
        holder.destinationText.setText(rideInstruction.getDestination().getName());
        holder.originText.setText(rideInstruction.getOrigin().getName());
        holder.originLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStationClick.onStationClick(rideInstruction.getStations().get(0));
            }
        });
        holder.destinationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStationClick.onStationClick(rideInstruction.getStations().
                        get(rideInstruction.getStations().size()-1));
            }
        });
        holder.stationsText.setText(rideInstruction.getSections().size()+" arrêts "+"("+rideInstruction.getDuration()/60+" minutes )");
        holder.takeText.setText("Prendre le "+rideInstruction.getTransportMean().getName());
        holder.separationView.setBackgroundColor(ContextCompat.getColor(context
                ,rideInstruction.getTransportMean().getColor()));
        holder.originCircle.setImageDrawable(ContextCompat.getDrawable(context,
                rideInstruction.getTransportMean().getStationCirleDrawableId()));
        holder.destinationCircle.setImageDrawable(ContextCompat.getDrawable(context,
                rideInstruction.getTransportMean().getStationCirleDrawableId()));
        final ArrayList<Station> stations = rideInstruction.getStations();
        stations.remove(0);
        stations.remove(stations.size()-1);
        final StationInsideLineAdapter stationInsideLineAdapter =
                new StationInsideLineAdapter(context,
                        stations,null,true);
        holder.stationsList.setVisibility(View.GONE);
        holder.stationsList.setAdapter(new StationInsideRideInstructionAdapter(context, stations, new StationInsideRideInstructionAdapter.OnStationClickListener() {
            @Override
            public void onStationClick(Station station) {
                onStationClick.onStationClick(station);
            }
        }));
        holder.stationsList.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        if (expandedStations.get(position))
        {
            holder.stationsList.setVisibility(View.VISIBLE);
            com.archidni.archidni.UiUtils.ViewUtils.changeTextViewState(context,holder.stationsText,
                    R.drawable.ic_keyboard_arrow_up_black_24dp, com.archidni.archidni.UiUtils.ViewUtils.DIRECTION_LEFT);
        }
        else
        {
            holder.stationsList.setVisibility(View.GONE);
            com.archidni.archidni.UiUtils.ViewUtils.changeTextViewState(context,holder.stationsText,
                    R.drawable.ic_keyboard_arrow_down_black_24dp, com.archidni.archidni.UiUtils.ViewUtils.DIRECTION_LEFT);
        }

        holder.stationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = expandedStations.get(position);
                expandedStations.set(position,!expanded);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pathInstructions.size();
    }

    class WaitInstructionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView titleText;
        @BindView(R.id.text_take_lines)
        TextView takeLinesText;
        @BindView(R.id.recyclerView)
        RecyclerView linesList;
        @BindView(R.id.icon_wait)
        ImageView waitIcon;
        @BindView(R.id.container)
        View container;

        public WaitInstructionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    class RideInstructionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_transport_mode)
        ImageView transportModeIcon;
        @BindView(R.id.text_origin)
        TextView originText;
        @BindView(R.id.text_destination)
        TextView destinationText;
        @BindView(R.id.text_stations_number)
        TextView stationsText;
        @BindView(R.id.text_take)
        TextView takeText;
        @BindView(R.id.view_separation)
        View separationView;
        @BindView(R.id.image_origin_circle)
        ImageView originCircle;
        @BindView(R.id.image_destination_circle)
        ImageView destinationCircle;
        @BindView(R.id.list_stations)
        RecyclerView stationsList;
        @BindView(R.id.layout_origin)
        View originLayout;
        @BindView(R.id.layout_destination)
        View destinationLayout;
        @BindView(R.id.layout_stations)
        View stationsLayout;
        @BindView(R.id.container)
        View container;
        public RideInstructionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class WalkInstructionViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_type)
        TextView typeText;
        @BindView(R.id.text_instruction)
        TextView instructionText;
        @BindView(R.id.text_duration)
        TextView durationText;
        @BindView(R.id.text_distance)
        TextView distanceText;
        @BindView(R.id.container)
        View container;


        public WalkInstructionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemSelected
    {
        void onItemSelected (PathInstruction pathInstruction,int position);
    }
}
