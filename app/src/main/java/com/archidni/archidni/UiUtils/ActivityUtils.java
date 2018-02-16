package com.archidni.archidni.UiUtils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by nouno on 16/02/2018.
 */

public class ActivityUtils {
    public static void hideKeyboard (Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().
                        getWindowToken(),
                0);
    }
}
