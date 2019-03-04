package com.archidni.archidni.Data.Users;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AccessToken;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.User;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.Ui.UpdateRequiredActivity;

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
    private static String USER_URL = "api/v1/user";

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

                    //root = new JSONObject(response);
                    //JSONObject userObject = root.getJSONObject("user");
                    //JSONObject tokensObject = root.getJSONObject("tokens");
                    //User user = getUser(userObject);
                    //parseAndSaveTokens(context,tokensObject);
                    signupRequestCallback.onSuccess();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                   if (error.networkResponse!=null&&error.networkResponse.statusCode==401)
                   {
                       signupRequestCallback.onUserAlreadyExists();
                   }
                   else
                   {
                       if (error.networkResponse!=null&&error.networkResponse.statusCode==410)
                       {
                           displayVersionIncorrect();
                       }
                       else
                       {
                           signupRequestCallback.onNetworkError();
                       }
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

    public void signupWithoutCode (final Context context, final String email, final String password,
                                   final String firstName, final String lastName,
                                   final SignupWithoutCodeRequestCallback signupRequestCallback)
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
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null&&error.networkResponse.statusCode==401)
                {
                    signupRequestCallback.onUserAlreadyExists();
                }
                else
                {
                    if (error.networkResponse!=null&&error.networkResponse.statusCode==410)
                    {
                        displayVersionIncorrect();
                    }
                    else
                    {
                        signupRequestCallback.onNetworkError();
                    }
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

    private void displayVersionIncorrect()
    {
        Context context = App.getAppContext();
        Intent intent = new Intent(context, UpdateRequiredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
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
                if (error.networkResponse!=null&&error.networkResponse.statusCode==401)
                {
                    loginRequestCallback.onEmailOrPasswordIncorrect();
                }
                else
                {
                    if (error.networkResponse!=null&&error.networkResponse.statusCode==410)
                    {
                        displayVersionIncorrect();
                    }
                    else
                    {
                        if (error.networkResponse!=null&&error.networkResponse.statusCode==403)
                        {
                            loginRequestCallback.onUserAlreadyConnected();
                        }
                        else
                            if (error.networkResponse!=null&&error.networkResponse.statusCode==402)
                            {
                                loginRequestCallback.onEmailNotVerified();
                            }
                            else
                                loginRequestCallback.onNetworkError();
                    }
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


    public void loginWithoutCode (final Context context, final String email, final String password,
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
                if (error.networkResponse!=null&&error.networkResponse.statusCode==401)
                {
                    loginRequestCallback.onEmailOrPasswordIncorrect();
                }
                else
                {
                    if (error.networkResponse!=null&&error.networkResponse.statusCode==410)
                    {
                        displayVersionIncorrect();
                    }
                    else
                    {
                        if (error.networkResponse!=null&&error.networkResponse.statusCode==403)
                        {
                            loginRequestCallback.onUserAlreadyConnected();
                        }
                        else
                        if (error.networkResponse!=null&&error.networkResponse.statusCode==402)
                        {
                            loginRequestCallback.onEmailNotVerified();
                        }
                        else
                            loginRequestCallback.onNetworkError();
                    }
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

    public void verifyEmailCode (final Context context, final String userEmail, final String password, final String verifCode,
                                 final EmailVerificationRequestCallback emailVerificationRequestCallback)
    {
        cancelRequests(context);
        String url = SharedPrefsUtils.getServerUrl(context)+"/"+USER_URL+"/verify-code";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject userObject = jsonObject.getJSONObject("user");
                    User user = getUser(userObject);
                    parseAndSaveTokens(context,jsonObject.getJSONObject("tokens"));
                    emailVerificationRequestCallback.onSuccess(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null)
                {
                    if (error.networkResponse.statusCode == 400)
                    {
                        emailVerificationRequestCallback.onCodeIncorrect();
                    }
                }
                else
                {
                    emailVerificationRequestCallback.onNetworkError();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
            LinkedHashMap<String,String> map = new LinkedHashMap<>();
            map.put("email",userEmail);
            map.put("password",password);
            map.put("verif_code",verifCode);
            return map;
        }};
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


    public void resendVerificationEmail (Context context, final String email, final EmailVerificationCodeResendCallback emailVerificationCodeResendCallback)
    {
        cancelRequests(context);
        String url = SharedPrefsUtils.getServerUrl(context) +"/"+ USER_URL+"/resend-code";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                emailVerificationCodeResendCallback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null)
                {
                    emailVerificationCodeResendCallback.onEmailAlreadyVerified();
                }
                else
                {
                    emailVerificationCodeResendCallback.onNetworkError();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("email",email);
                return map;
            }
        };
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,getTag());
    }

    public void updatePassword (final Context context, final String oldPassword, final String newPassword
            , final PasswordUpdateRequestCallback passwordUpdateRequestCallback)
    {
        User user = SharedPrefsUtils.getConnectedUser(context);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.PUT,
                String.format("%s/%s/%s/%s", SharedPrefsUtils.getServerUrl(App.getAppContext()),
                        USER_URL, user.getId() + "", "update-password"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            OauthStringRequest.updateTokens(context,
                                    responseObject.getJSONObject("tokens").toString());
                            passwordUpdateRequestCallback.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            passwordUpdateRequestCallback.onNetworkError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse!=null)
                        {
                            if (error.networkResponse.statusCode==408)
                            {
                                passwordUpdateRequestCallback.onOldPasswordIncorrect();
                            }
                            else
                            {
                                passwordUpdateRequestCallback.onNetworkError();
                            }
                        }
                        else
                        {
                            passwordUpdateRequestCallback.onNetworkError();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("old_password",oldPassword);
                map.put("new_password",newPassword);
                return map;
            }
        };
        oauthStringRequest.performRequest("USER");
    }

    public void disconnectUser (Context context, final InfoUpdateDisconnectRequestCallback infoUpdateDisconnectRequestCallback)
    {
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET,
                String.format("%s/%s/%s", SharedPrefsUtils.getServerUrl(context), USER_URL, "disconnect"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        infoUpdateDisconnectRequestCallback.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoUpdateDisconnectRequestCallback.onNetworkError();
            }
        });
        oauthStringRequest.performRequest("USER");
    }

    public void updateUserInfo (final Context context, final User newUser, final InfoUpdateDisconnectRequestCallback infoUpdateDisconnectRequestCallback)
    {
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.PUT,
                String.format("%s/%s/%s/%s",SharedPrefsUtils.getServerUrl(context),USER_URL, newUser.getId() + "", "update"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPrefsUtils.saveString(context,
                                SharedPrefsUtils.SHARED_PREFS_ENTRY_USER_OBJECT,
                                newUser.toJson());
                        infoUpdateDisconnectRequestCallback.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoUpdateDisconnectRequestCallback.onNetworkError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("first_name",newUser.getFirstName());
                map.put("last_name",newUser.getLastName());
                return map;
            }
        };
        oauthStringRequest.performRequest("USER");
    }



    public interface SignupRequestCallback {
        public void onSuccess ();
        public void onUserAlreadyExists();
        public void onNetworkError();
    }

    public interface SignupWithoutCodeRequestCallback {
        public void onSuccess (User user);
        public void onUserAlreadyExists();
        public void onNetworkError();
    }

    public interface EmailVerificationCodeResendCallback {
        public void onSuccess();
        public void onEmailAlreadyVerified();
        public void onNetworkError();
    }

    public interface LoginRequestCallback {
        public void onSuccess (User user);
        public void onEmailOrPasswordIncorrect();
        public void onUserAlreadyConnected();
        public void onEmailNotVerified();
        public void onNetworkError();
    }

    public interface EmailVerificationRequestCallback {
        public void onSuccess(User user);
        public void onCodeIncorrect();
        public void onNetworkError();
    }

    public interface InfoUpdateDisconnectRequestCallback {
        public void onSuccess();
        public void onNetworkError();
    }

    public interface PasswordUpdateRequestCallback {
        public void onSuccess ();
        public void onOldPasswordIncorrect();
        public void onNetworkError();
    }
}
