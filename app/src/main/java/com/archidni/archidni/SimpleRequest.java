package com.archidni.archidni;

import com.android.volley.Response;

public class SimpleRequest extends NetworkRequest {

    public SimpleRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

}
