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

import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.R;
import com.archidni.archidni.UiUtils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<Notification> notifications;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification ,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.transportModeImage.setImageDrawable(ContextCompat.getDrawable(context,
                notification.getTransportMean().getIconEnabled()));
        holder.titleText.setText(notification.getTitle());
        holder.descriptionText.setText(notification.getDescription());
        if (notification.getType()==Notification.TYPE_PERTURBATION)
        {
            //ViewUtils.changeTextViewState(context,holder.notificationType,R.drawable.ic_notifications_none_red_24dp,
            //        R.color.colorRed,ViewUtils.DIRECTION_LEFT);
            holder.notificationType.setText(R.string.alert_perturbation);
            holder.notificationType.setTextColor(ContextCompat.getColor(context,R.color.colorRed));
        }
        else
        {
            //ViewUtils.changeTextViewState(context,holder.notificationType,R.drawable.ic_info_green_24dp,
            //        R.color.colorGreen,ViewUtils.DIRECTION_LEFT);
            holder.notificationType.setTextColor(ContextCompat.getColor(context,R.color.colorGreen));
            holder.notificationType.setText(R.string.alert_information);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_transport_mode)
        ImageView transportModeImage;
        @BindView(R.id.text_title)
        TextView titleText;
        @BindView(R.id.text_description)
        TextView descriptionText;
        @BindView(R.id.text_type)
        TextView notificationType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
