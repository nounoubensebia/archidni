package com.archidni.archidni.Data.Reports;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.archidni.archidni.App;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.Reports.DisruptionReport;
import com.archidni.archidni.Model.Reports.PathReport;
import com.archidni.archidni.Model.Reports.Report;
import com.archidni.archidni.OauthStringRequest;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportsOnlineDataStore implements ReportsDataStore {

    private static final String URL_DISRUPTION = "/user-reports/disruption/create";
    private static final String URL_OTHER = "/user-reports/other/create";

    @Override
    public void sendDisruptionReport(final DisruptionReport disruptionReport, final ReportsRepository.OnCompleteListener onComplete) {
        Context context = App.getAppContext();
        String url = String.format("%s%s", SharedPrefsUtils.getServerUrl(context),URL_DISRUPTION);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onComplete.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("user_id",disruptionReport.getUser().getId()+"");
                if (disruptionReport.getDisruptionSubject().getLine()!=null)
                {
                    map.put("line_id",disruptionReport.getDisruptionSubject().getLine().getId()+"");
                    map.put("transport_mode_id",disruptionReport.getDisruptionSubject().getLine()
                            .getTransportMean().getId()+"");
                }
                map.put("transport_mode_id",disruptionReport.getDisruptionSubject().getTransportMean().getId()+"");
                map.put("description",disruptionReport.getDescription());
                return map;
            }
        };
        oauthStringRequest.performRequest("REPORT");
    }

    @Override
    public void sendOtherReport(final Report report, final ReportsRepository.OnCompleteListener onComplete) {
        Context context = App.getAppContext();
        String url = String.format("%s%s", SharedPrefsUtils.getServerUrl(context),URL_OTHER);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onComplete.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("user_id",report.getUser().getId()+"");
                map.put("description",report.getDescription());
                return map;
            }
        };
        oauthStringRequest.performRequest("REPORT");
    }

    @Override
    public void sendPathReport(final PathReport report, final ReportsRepository.OnCompleteListener onComplete) {
        Context context = App.getAppContext();
        String url = String.format("%s%s", SharedPrefsUtils.getServerUrl(context),URL_OTHER);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onComplete.onComplete();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onComplete.onError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                map.put("user_id",report.getUser().getId()+"");
                if (report.getDescription()!=null)
                    map.put("description",report.getDescription());

                map.put("path_data",new Gson().toJson(report.getPath()));

                if (report.isGood())
                    map.put("is_good",1+"");
                else
                    map.put("is_good",0+"");

                return map;
            }
        };
        oauthStringRequest.performRequest("REPORT");
    }
}
