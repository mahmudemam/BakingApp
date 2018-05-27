package com.udacity.nd.projects.bakingapp.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static RequestQueue mRequestQueue;

    public interface FetchCallback {
        public void onFetchResult(Object result);
    }

    private NetworkUtils() {
    }

    private static synchronized RequestQueue getInstance(Context context) {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static void fetchRecipes(Context context, final String URL, final FetchCallback callback) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onFetchResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        getInstance(context).add(jsonRequest);
    }
}
