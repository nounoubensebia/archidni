package com.archidni.archidni.Ui.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineInsideWaitInstructionAdapter extends RecyclerView.Adapter<LineInsideWaitInstructionAdapter.ViewHolder> {

    private Context context;

    private ArrayList<WaitLine> waitLines;

    private OnItemClick onItemClick;


    public LineInsideWaitInstructionAdapter(Context context, ArrayList<WaitLine> waitLines, OnItemClick onItemClick) {
        this.context = context;
        this.waitLines = waitLines;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_inside_walk_instruction,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WaitLine waitLine = waitLines.get(position);
        holder.lineNameText.setText(waitLine.getLine().getName()+" vers "+waitLine.getDestination());
        if (waitLine.isExactWaitingTime())
            holder.durationText.setText("Temps d'attente "+waitLine.getTime()+" minutes");
        else
            holder.durationText.setText("Temps d'attente moyen "+waitLine.getTime()+ " minutes");
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(waitLine);
            }
        });
        ViewUtils.changeTextViewState(context,holder.lineNameText,
                waitLine.getLine().getTransportMean().getIconEnabled(),ViewUtils.DIRECTION_LEFT);
    }

    @Override
    public int getItemCount() {
        return waitLines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_line_name)
        TextView lineNameText;
        @BindView(R.id.text_duration)
        TextView durationText;
        @BindView(R.id.container)
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick {
        void onItemClick (WaitLine waitLine);
    }
}
