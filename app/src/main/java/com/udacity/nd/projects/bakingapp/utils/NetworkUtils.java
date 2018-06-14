package com.udacity.nd.projects.bakingapp.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.nd.projects.bakingapp.idlingResource.SimpleIdlingResource;

import org.json.JSONArray;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static RequestQueue mRequestQueue;

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

    public static void fetchRecipes(Context context, final String URL, final FetchCallback callback, @Nullable final SimpleIdlingResource idlingResource) {
        Log.d(TAG, "fetchRecipes");

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            Log.d(TAG, "IdlingResource state=false");
            idlingResource.setIdleState(false);
        }

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onFetchResult(response);

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        });

        getInstance(context).add(jsonRequest);
    }

    public interface FetchCallback {
        public void onFetchResult(Object result);
    }
}
