package com.archidni.archidni;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public abstract class NetworkRequest extends StringRequest {

    private Response.Listener listener;

    public NetworkRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.listener = listener;
    }

    public void performRequest (String requestTag)
    {
        AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(this,requestTag);
    }
}
