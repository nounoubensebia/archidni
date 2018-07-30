package com.archidni.archidni;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.Data.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class OauthStringRequest extends NetworkRequest {

    private static final String OauthRefreshUri = "/oauth/token";
    Response.Listener<String> listener;


    public OauthStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.listener = listener;
    }
    @Override
    public void performRequest (final String tag)
    {
        final Context context = App.getAppContext();
        final AccessToken accessToken = SharedPrefsUtils.getAccessToken(context);
        if (accessToken.hasExpired())
        {
            StringRequest refreshTokenRequest = new StringRequest(Method.POST,
                    SharedPrefsUtils.getServerUrl(context) + OauthRefreshUri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        updateTokens(context,response);
                        AccessToken accessToken1 = SharedPrefsUtils.getAccessToken(context);
                        makeOauthRequest(context,accessToken1,tag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        OauthStringRequest.this.getErrorListener();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorText = error.getMessage();
                    OauthStringRequest.this.getErrorListener().onErrorResponse(error);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put("grant_type","refresh_token");
                    linkedHashMap.put("client_id","2");
                    linkedHashMap.put("client_secret","YxXYNvrTWIxTpZQaqINcGmUlIl6o6TqJziVB601G");
                    linkedHashMap.put("scope","*");
                    linkedHashMap.put("refresh_token",SharedPrefsUtils.getRefreshToken(context));
                    return linkedHashMap;
                }
            };
            AppSingleton.getInstance(context).addToRequestQueue(refreshTokenRequest,tag);
        }
        else
        {
            makeOauthRequest(context,accessToken,tag);
        }
    }

    private void makeOauthRequest (Context context, final AccessToken accessToken,String tag)
    {
        StringRequest request = new StringRequest(getMethod(),getUrl(),listener,getErrorListener())
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map map = new LinkedHashMap();
                map.put("authorization","Bearer "+accessToken.getToken());
                return map;
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(request,tag);
    }

    private void updateTokens (Context context,String response) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(response);
        String accessTokenString = jsonObject.getString("access_token");
        long ttl = jsonObject.getInt("expires_in");
        long timeAcquired = (System.currentTimeMillis()/1000)-60;
        AccessToken newAccessToken = new AccessToken(accessTokenString,ttl,timeAcquired);
        SharedPrefsUtils.setAccessToken(context,newAccessToken);
        String refreshToken = jsonObject.getString("refresh_token");
        SharedPrefsUtils.setRefreshToken(context,refreshToken);
    }
}
