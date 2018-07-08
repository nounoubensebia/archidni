package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainListPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 08/02/2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private ArrayList<MainListPlace> mainListPlaces;
    private Coordinate userCoordinate;
    private Coordinate sortCoordinate;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public PlaceAdapter(Context context, ArrayList<? extends  MainListPlace> places, final Coordinate userCoordinate,
                        OnItemClickListener onItemClickListener) {
        this.mainListPlaces = new ArrayList<>(places);
        this.sortCoordinate = sortCoordinate;
        this.userCoordinate = userCoordinate;
        this.context = context;
        this.setHasStableIds(true);
        this.onItemClickListener = onItemClickListener;
    }

    public void updateItems(ArrayList<? extends MainListPlace> newItems, final Coordinate userCoordinate) {
        ArrayList<MainListPlace> places = new ArrayList<>(newItems);

        this.userCoordinate = userCoordinate;
        this.mainListPlaces.clear();
        this.mainListPlaces.addAll(places);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return mainListPlaces.get(position).getId();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MainListPlace place = mainListPlaces.get(position);
        holder.nameText.setText(place.getName());
        holder.nameText.setTextColor(ContextCompat.getColor(context,place.getColor()));
        holder.mainText.setVisibility(View.GONE);
        if (userCoordinate!=null)
        {
            holder.mainText.setVisibility(View.VISIBLE);
            int distance = GeoUtils.distance(userCoordinate,place.getCoordinate());
            holder.mainText.setText("à "+StringUtils.getTextFromDistance(distance)+" de votre position");
        }

        if (place.hasSecondaryText())
        {
            holder.secondaryText.setVisibility(View.VISIBLE);
            holder.secondaryText.setText(place.getSecondaryText());
        }
        else
        {
            holder.secondaryText.setVisibility(View.GONE);
        }

        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,
                place.getIcon()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainListPlaces.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.text_main_text)
        TextView mainText;
        @BindView(R.id.text_secondary_text)
        TextView secondaryText;
        @BindView(R.id.image_icon)
        ImageView imageView;
        @BindView(R.id.container)
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick (MainListPlace mainListPlace);
    }
}
