/* $Id$ */
package com.movies.popularmovies.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.movies.popularmovies.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class RequestProcessor {
    Context context;
    RequestProcessorListener mListener;
    int method;
    JSONObject json;

    static RequestQueue queue;
    boolean attach;

    HashMap<String, String> headers;
    private String startTime = "";//no i18n

    //Default constructor for normal JSON requests - POST
    public RequestProcessor(Context context, int method) {

        this.context = context;
        this.method = method;
        headers = new HashMap<>();
        //ZohoPeople/3.0 (Android 4.5; SAMSUNG GT-I9505)
        //String UAString = "ZohoAssist/" + Utils.getAppVersionName(context) + " (Android " + Utils.getAndroidVersion() + "; " + Utils.getDeviceName() + ")";//no i18n
        //headers.put("User-agent", UAString);//no i18n
        headers.put("X-ZCSRF-TOKEN", "ze_csrf_param=453ee0d0-5c7e-446b-a856-927ec47f87e2");//no i18n
    }

    public RequestProcessor(Context context, int method, JSONObject json) {
        this.context = context;
        this.method = method;
        this.json = json;
        headers = new HashMap<>();
        //ZohoAssist/1.0.2 (Android 6.0.1; OnePlus ONE A2003)
        //String UAString = "ZohoAssist/" + Utils.getAppVersionName(context) + " (Android " + Utils.getAndroidVersion() + "; " + Utils.getDeviceName() + ")";//no i18n
        //headers.put("User-agent", UAString);//no i18n
        headers.put("X-ZCSRF-TOKEN", "ze_csrf_param=453ee0d0-5c7e-446b-a856-927ec47f87e2");//no i18n
    }

    //use for normal JSON response retrieval
    public RequestProcessor setListener(RequestProcessorListener listener) {
        mListener = listener;
        return this;
    }

    public void execute(String url, String description) {

        final Request request;
        if (/*method == Request.Method.PUT && */json != null) {
            request = new JsonObjectRequest(method, url, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject s) {
                            if(mListener !=null) {
                                mListener.onSuccess(s.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mListener != null) {
                        mListener.onError(volleyError);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
        } else {
            // Request a string response
            request = new StringRequest(method, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("onResponse ", response);//no i18n
                            if (response == null) {
                                Log.e("Null Response", "Response is Null");//no i18n
                            } else {
                                // Result handling
                                if (mListener != null) {
                                    mListener.onSuccess(response);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (mListener != null) {
                        mListener.onError(error);
                    }
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;

                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //JAnalyticsEventDetails.endApiTracking(startTime, response.statusCode, new String(response.data));
                    return super.parseNetworkResponse(response);
                }
            };
        }
        /*
        else if(method == Request.Method.DELETE) {

            request = new StringRequest(method, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("RP", "onResponse() called with: " + "s = [" + s + "]");//no i18n

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            });
        }*/


        Log.d("execute ", url);//no i18n
        if (GeneralUtils.isNetworkAvailable(context)) {
            if (mListener != null) {
                String methodStr = "";//no i18n

                if (method == Request.Method.PUT) {
                    methodStr = "PUT";//no i18n
                }
                if (method == Request.Method.GET) {
                    methodStr = "GET";//no i18n
                }
                if (method == Request.Method.POST) {
                    methodStr = "POST";//no i18n
                }
                if (method == Request.Method.DELETE) {
                    methodStr = "DELETE";//no i18n
                }
                //startTime = JAnalyticsEventDetails.startApiTracking(url, methodStr, String.valueOf(80), description);
                mListener.onLoading();
            }
            // Add the request to the queue
            request.setRetryPolicy(new DefaultRetryPolicy(50000, 10, 2.0F));
            //http://stackoverflow.com/questions/35701501/throwing-outofmemoryerror-pthread-create-1040kb-stack-failed-try-again-when
            if (queue == null) {
                queue = Volley.newRequestQueue(context);
            }
            queue.add(request);
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void execute(String url) {
        execute(url, null);
    }


    private class CustomJsonRequest extends JsonObjectRequest {

        public CustomJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response parseNetworkResponse(NetworkResponse networkResponse) {
            int status = networkResponse.statusCode;
            //JAnalyticsEventDetails.endApiTracking(startTime, status, "");//no i18n
            Log.d("StatusCode", "Status " + status);//no i18n
            if (status == 200) {
                mListener.onSuccess(status + "");//no i18n
            }
            return super.parseNetworkResponse(networkResponse);
        }

    }

}

