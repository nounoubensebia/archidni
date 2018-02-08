package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noure on 08/02/2018.
 */

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private ArrayList<Line> lines;
    private Context context;

    public LineAdapter(ArrayList<Line> lines, Context context) {
        this.lines = new ArrayList<>(lines);
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line ,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return lines.get(position).getId();
    }

    public void updateItems(ArrayList<Line> newItems) {
        lines.clear();
        lines.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Line line = lines.get(position);
        holder.nameText.setText(line.getName());
        holder.nameText.setTextColor(ContextCompat.getColor(context,line.getTransportMean().getColor()));
        holder.originText.setText(line.getOrigin().getName());
        holder.destinationText.setText(line.getDestination().getName());
        holder.separationView.setBackgroundResource(line.getTransportMean().getColor());
        holder.transportMeanIcon.setImageDrawable(ContextCompat.getDrawable(context,
                line.getTransportMean().getIconEnabled()));
        ViewUtils.changeTextViewState(context,holder.originText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
        ViewUtils.changeTextViewState(context,holder.destinationText,
                line.getTransportMean().getCircleDrawable(),ViewUtils.DIRECTION_LEFT);
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_transport_mean_icon)
        ImageView transportMeanIcon;
        @BindView(R.id.text_name)
        TextView nameText;
        @BindView(R.id.text_origin)
        TextView originText;
        @BindView(R.id.separation_view)
        View separationView;
        @BindView(R.id.text_destination)
        TextView destinationText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
