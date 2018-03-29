package com.archidni.archidni.Data;

import android.content.Context;

import com.archidni.archidni.AppSingleton;

public abstract class OnlineDataStore {
    public abstract String getTag ();
    public void cancelRequests (Context context)
    {
        AppSingleton.getInstance(context).getRequestQueue().cancelAll(getTag());
    }
}
