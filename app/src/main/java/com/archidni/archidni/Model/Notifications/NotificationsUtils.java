package com.archidni.archidni.Model.Notifications;

import android.content.Context;

import com.archidni.archidni.Data.Favorites.FavoritesRepository;
import com.archidni.archidni.Model.Favorites;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.Transport.TransportUtils;

import java.util.ArrayList;

public class NotificationsUtils {
    public static ArrayList<Notification> getFavoritesNotifications (Context context,ArrayList<Notification> notifications)
    {
        //TODO show no favorites message
        FavoritesRepository favoritesRepository = new FavoritesRepository();
        Favorites favorites = favoritesRepository.getFavorites(context);
        if (favorites!=null)
        {
            ArrayList<Notification> favoritesNotifications = new ArrayList<>();
            for (Notification notification : notifications)
            {
                if (notification.getLines().size()>0)
                    for (LineSkeleton line:notification.getLines())
                    {
                        if (TransportUtils.containsLine(line.getId(),favorites.getLines()))
                        {
                            favoritesNotifications.add(notification);
                        }
                    }
                    else
                        for (Line line:favorites.getLines())
                        {
                            if (line.getTransportMean().getId()==notification.getTransportMean().getId())
                            {
                                favoritesNotifications.add(notification);
                            }
                        }
            }
            return favoritesNotifications;
        }
        else
        {
            return new ArrayList<>();
        }
    }
}
