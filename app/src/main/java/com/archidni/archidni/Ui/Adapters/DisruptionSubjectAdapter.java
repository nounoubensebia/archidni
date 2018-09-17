package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archidni.archidni.Model.Reports.DisruptionSubject;
import com.archidni.archidni.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisruptionSubjectAdapter extends RecyclerView.Adapter<DisruptionSubjectAdapter.ViewHolder> {

    private Context context;
    private List<DisruptionSubject> disruptionSubjects;
    private OnItemClick onItemClick;

    public DisruptionSubjectAdapter(Context context, List<DisruptionSubject> disruptionSubjects, OnItemClick onItemClick) {
        this.context = context;
        this.disruptionSubjects = disruptionSubjects;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disruption ,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DisruptionSubject disruptionSubject = disruptionSubjects.get(position);

        if (disruptionSubject.getLine()!=null)
        {
            holder.textView.setText(disruptionSubject.getLine().getName());
            holder.transportModeIcon.setImageDrawable(ContextCompat.getDrawable(context,
                    disruptionSubject.getLine().getTransportMean().getIconEnabled()));
        }
        else
        {
            holder.textView.setText("Tout le réseau de "+disruptionSubject.getTransportMean().getName());
            holder.transportModeIcon.setImageDrawable(ContextCompat.getDrawable(context,
                    disruptionSubject.getTransportMean().getIconEnabled()));
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(disruptionSubject);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disruptionSubjects.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView textView;
        @BindView(R.id.image_transport_mean_icon)
        ImageView transportModeIcon;
        @BindView(R.id.container)
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick {
        void onItemClick(DisruptionSubject disruptionSubject);
    }
}
