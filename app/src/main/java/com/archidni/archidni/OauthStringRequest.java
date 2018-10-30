package com.archidni.archidni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Ui.Login.LoginActivity;
import com.archidni.archidni.Ui.SplashActivity;
import com.archidni.archidni.Ui.UpdateRequiredActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
        boolean checkApi = SharedPrefsUtils.checkRequestNumber(App.getAppContext());
        if (checkApi)
        {
            long currentTime = TimeUtils.getCurrentTimeInSeconds();
            if (currentTime>1541872522)
            {
                SharedPrefsUtils.destroyApp(App.getAppContext());
                Intent intent = new Intent(App.getAppContext(), SplashActivity.class);
                App.getAppContext().startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                App.getAppContext().startActivity(intent);
            }
            StringRequest checkingTimeRequest = new StringRequest(Method.GET,
                    "http://api.timezonedb.com/v2.1/get-time-zone?key=UOH34KGEMFNV&format=json&by=zone&zone=Africa/Algiers",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                long timeStamp = jsonObject.getLong("timestamp");
                                if (timeStamp>1541872522)
                                {
                                    SharedPrefsUtils.destroyApp(App.getAppContext());
                                    Intent intent = new Intent(App.getAppContext(), SplashActivity.class);
                                    App.getAppContext().startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    App.getAppContext().startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                OauthStringRequest.this.getErrorListener();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    OauthStringRequest.this.getErrorListener();
                }
            });
            AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(checkingTimeRequest,"TIME");
        }
        final Context context = App.getAppContext();
        final AccessToken accessToken = SharedPrefsUtils.getAccessToken(context);
        if (accessToken==null || accessToken.hasExpired())
        {
            StringRequest refreshTokenRequest = new StringRequest(Method.POST,
                    SharedPrefsUtils.getServerUrl(context) + OauthRefreshUri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        updateTokens(context,response);
                        AccessToken accessToken1 = SharedPrefsUtils.getAccessToken(context);
                        makeOauthRequest(accessToken1,tag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        OauthStringRequest.this.getErrorListener();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse!=null&&error.networkResponse.data!=null) {
                        try {
                            String body = new String(error.networkResponse.data,"UTF-8");
                            JSONObject jsonObject = new JSONObject(body);
                            String message = jsonObject.getString("message");
                            if (message.equals("The refresh token is invalid."))
                            {
                                disconnectUser();
                            }
                            Log.e("REFRESH TOKEN ERROR",body);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e)
                        {

                        }
                    }
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
            makeOauthRequest(accessToken,tag);
        }
    }

    private void makeOauthRequest (final AccessToken accessToken,String tag)
    {
        Context context = App.getAppContext();
        StringRequest request = new StringRequest(getMethod(), getUrl(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //OauthStringRequest.this.getErrorListener().onErrorResponse(error);
                if (error.networkResponse!=null)
                {
                    if (error.networkResponse.statusCode==401)
                    {
                        disconnectUser();
                        return;
                    }
                    if (error.networkResponse.statusCode==410)
                    {
                        displayVersionIncorrect();
                        return;
                    }
                    OauthStringRequest.this.getErrorListener().onErrorResponse(error);
                }
                else
                {
                    OauthStringRequest.this.getErrorListener().onErrorResponse(error);
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map map = new LinkedHashMap();
                map.put("authorization","Bearer "+accessToken.getToken());
                map.put("app-version",App.VERSION+"");
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return OauthStringRequest.this.getParams();
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(request,tag);
    }


    private void displayVersionIncorrect()
    {
        Context context = App.getAppContext();
        Intent intent = new Intent(context, UpdateRequiredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private void disconnectUser()
    {
        Context context = App.getAppContext();
        Toast.makeText(context,"Authentification échouée",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
        SharedPrefsUtils.disconnectUser(context);

    }

    public static void updateTokens (Context context,String response) throws JSONException
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
