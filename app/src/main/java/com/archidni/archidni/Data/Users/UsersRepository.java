package com.archidni.archidni.Data.Users;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AccessToken;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.LineStationSuggestions.LineStationDataStore;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nouno on 16/02/2018.
 */

public class UsersRepository extends OnlineDataStore {
    private static  String SIGNUP_URL = "/api/v1/user/signup";
    private static  String LOGIN_URL = "/api/v1/user/login";

    public void signup (final Context context, final String email, final String password,
                        final String firstName, final String lastName,
                        final SignupRequestCallback signupRequestCallback)
    {
        cancelRequests(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SharedPrefsUtils.getServerUrl(context)+SIGNUP_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject root = null;
                try {
                    root = new JSONObject(response);
                    JSONObject userObject = root.getJSONObject("user");
                    JSONObject tokensObject = root.getJSONObject("tokens");
                    User user = getUser(userObject);
                    parseAndSaveTokens(context,tokensObject);
                signupRequestCallback.onSuccess(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                    signupRequestCallback.onNetworkError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                   if (error.networkResponse!=null)
                   {
                       signupRequestCallback.onUserAlreadyExists();
                   }
                   else
                   {
                       signupRequestCallback.onNetworkError();
                   }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("email",email);
                map.put("password",password);
                map.put("first_name",firstName);
                map.put("last_name",lastName);
                return map;
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,getTag());
    }

    public void login (final Context context, final String email, final String password,
                       final LoginRequestCallback loginRequestCallback)
    {
        cancelRequests(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SharedPrefsUtils.getServerUrl(context)+ LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            JSONObject userObject = root.getJSONObject("user");
                            JSONObject tokensObject = root.getJSONObject("tokens");
                            User user = getUser(userObject);
                            parseAndSaveTokens(context,tokensObject);
                            loginRequestCallback.onSuccess(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null)
                {
                    loginRequestCallback.onEmailOrPasswordIncorrect();
                }
                else
                {
                    loginRequestCallback.onNetworkError();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,getTag());
    }

    private User getUser (JSONObject userObject) throws JSONException {
        int id = userObject.getInt("id");
        String firstName = userObject.getString("first_name");
        String lastName = userObject.getString("last_name");
        String email = userObject.getString("email");
        return new User(id,email,firstName,lastName);
    }

    private void parseAndSaveTokens (Context context,JSONObject tokensJson) throws JSONException {
        int expiresIn = tokensJson.getInt("expires_in");
        String accessTokenString = tokensJson.getString("access_token");
        String refreshToken = tokensJson.getString("refresh_token");
        SharedPrefsUtils.setRefreshToken(context,refreshToken);
        long timeAquirred = (System.currentTimeMillis()/1000)-60;
        SharedPrefsUtils.setAccessToken(context,new AccessToken(accessTokenString,expiresIn,timeAquirred));
    }

    @Override
    public String getTag() {
        return "USERS";
    }


    public interface SignupRequestCallback {
        public void onSuccess (User user);
        public void onUserAlreadyExists();
        public void onNetworkError();
    }

    public interface LoginRequestCallback {
        public void onSuccess (User user);
        public void onEmailOrPasswordIncorrect();
        public void onNetworkError();
    }
}
