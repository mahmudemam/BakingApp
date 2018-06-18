package com.udacity.nd.projects.bakingapp.widgets;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.udacity.nd.projects.bakingapp.data.Recipe;

public class IngredientsListViewService extends RemoteViewsService {
    public static final String RECIPE_KEY = "recipe";
    private static final String TAG = IngredientsListViewService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory, contains recipe=" + intent.hasExtra(RECIPE_KEY));
        return new IngredientsListViewFactory(getApplicationContext(), (Recipe) intent.getParcelableExtra(RECIPE_KEY));
    }
}
