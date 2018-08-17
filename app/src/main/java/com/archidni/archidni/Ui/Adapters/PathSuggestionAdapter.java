package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;

/**
 * Created by noure on 12/02/2018.
 */

public class PathSuggestionAdapter extends RecyclerView.Adapter<PathSuggestionAdapter.ViewHolder> {
    private ArrayList<Path> paths;
    private PathSettings pathSettings;
    private Context context;
    private OnClickListener onClickListener;

    public PathSuggestionAdapter(Context context,ArrayList<Path> paths,PathSettings pathSettings) {
        this.paths = paths;
        this.context = context;
        this.pathSettings = pathSettings;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path_suggestion, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Path path = paths.get(position);
        holder.arrivalText.setText("Arrivée à "+path.getEtaText(pathSettings.getDepartureTime()));
        holder.departureText.setText("Départ à "+ StringUtils.getTimeString(path.getPathSettings().getDepartureTime()));
        holder.etaMinutesText.setText((int)path.getDurationInMinutes()+"");
        holder.transportMean2Image.setVisibility(View.GONE);
        holder.transportMean3Image.setVisibility(View.GONE);
        if (path.getTransportMeans().size()>0)
        {
            Drawable transportMean1Drawable = ContextCompat.getDrawable(context,path.getTransportMeans().get(0).getIconEnabled());
            holder.transportMean1Image.setImageDrawable(transportMean1Drawable);
        }
        else
        {
            Drawable transportMean1Drawable = ContextCompat.getDrawable(context,R.drawable.ic_walk_green_24dp);
            holder.transportMean1Image.setImageDrawable(transportMean1Drawable);
        }
        if (path.getTransportMeans().size()>1)
        {
            Drawable transportMean2Drawable = ContextCompat.getDrawable(context,path.getTransportMeans().get(1).getIconEnabled());
            holder.transportMean2Image.setImageDrawable(transportMean2Drawable);
            holder.transportMean2Image.setVisibility(View.VISIBLE);
        }
        if (path.getTransportMeans().size()>2)
        {
            Drawable transportMean3Drawable = ContextCompat.getDrawable(context,path.getTransportMeans().get(2).getIconEnabled());
            holder.transportMean3Image.setImageDrawable(transportMean3Drawable);
            holder.transportMean3Image.setVisibility(View.VISIBLE);
        }
        if (onClickListener!=null)
        {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(path);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView arrivalText;
        TextView departureText;
        TextView etaMinutesText;
        ImageView transportMean1Image;
        ImageView transportMean2Image;
        ImageView transportMean3Image;
        View root;
        public ViewHolder(View itemView) {
            super(itemView);
            arrivalText = (TextView)itemView.findViewById(R.id.text_arrival);
            etaMinutesText =(TextView)itemView.findViewById(R.id.text_eta_minutes);
            departureText = itemView.findViewById(R.id.text_departure);
            transportMean1Image = (ImageView)itemView.findViewById(R.id.image_transport_mean_1);
            transportMean2Image = (ImageView)itemView.findViewById(R.id.image_transport_mean_2);
            transportMean3Image = (ImageView)itemView.findViewById(R.id.image_transport_mean_3);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnClickListener {
        public void onClick(Path path);
    }
}
