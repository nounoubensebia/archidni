package com.archidni.archidni.UiUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.archidni.archidni.Ui.Search.SearchActivity;

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
            case DIRECTION_UP : textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,
                    null,null);
                break;
            case DIRECTION_LEFT : textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,
                    null,null);
                break;
            case DIRECTION_RIGHT : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    drawable,null);
                break;
            case DIRECTION_DOWN : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    null,drawable);
                break;

        }
    }

    public static void changeTextViewState (Context context, TextView textView, int drawableResource, int direction)
    {
        Drawable drawable = ContextCompat.getDrawable(context,drawableResource);
        switch (direction)
        {
            case DIRECTION_UP : textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,
                    null,null);
                break;
            case DIRECTION_LEFT : textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,
                    null,null);
                break;
            case DIRECTION_RIGHT : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    drawable,null);
                break;
            case DIRECTION_DOWN : textView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    null,drawable);
                break;
        }
    }

    public static float dpToPx (Context context,float dp)
    {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float pxToDp (Context context,float px)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public  static  void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public static float getScreenHeightInDp(Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = activity.getResources().getDisplayMetrics().density;
        return outMetrics.heightPixels / density;
    }

    public static float getScreenWidthInDp(Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = activity.getResources().getDisplayMetrics().density;
        return outMetrics.widthPixels / density;
    }

    public static ViewStub deflate(View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            int index = ((ViewGroup) viewParent).indexOfChild(view);
            int inflatedId = view.getId();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            ((ViewGroup) viewParent).removeView(view);
            Context context = ((ViewGroup) viewParent).getContext();
            ViewStub viewStub = new ViewStub(context);
            viewStub.setInflatedId(inflatedId);
            viewStub.setLayoutParams(layoutParams);
            ((ViewGroup) viewParent).addView(viewStub, index);
            return viewStub;
        } else {
            throw new IllegalStateException("Inflated View has not a parent");
        }
    }

}
