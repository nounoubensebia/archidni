package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by noure on 02/02/2018.
 */

public class ViewUtils {
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_DOWN = 3;
    public static void changeTextViewState (Context context, TextView textView, int drawableResource, int textColorId, int direction)
    {
        textView.setTextColor(ContextCompat.getColor(context,textColorId));
        Drawable drawable = ContextCompat.getDrawable(context,drawableResource);
        switch (direction)
        {
            case DIRECTION_UP : textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
                break;
            case DIRECTION_LEFT : textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                break;
            case DIRECTION_RIGHT : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                break;
            case DIRECTION_DOWN : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable);
                break;
        }
    }
}
