package com.udacity.nd.projects.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;
import com.udacity.nd.projects.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by noname on 6/9/18.
 */

public class RecipeWidgetService extends IntentService implements NetworkUtils.FetchCallback {
    private static final String TAG = RecipeWidgetService.class.getSimpleName();
    public static final String ACTION_INGREDIENT_LIST = "ingredients";

    public RecipeWidgetService() {
        super(TAG);
    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_INGREDIENT_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_INGREDIENT_LIST.equals(action)) {
            handleActionIngredientList();
        }
    }

    @Override
    public void onFetchResult(Object result) {
        try {
            List<Recipe> mRecipes = JsonUtils.parseRecipes((JSONArray) result);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));

            RecipeAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, mRecipes.get(0));
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void handleActionIngredientList() {
        final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        NetworkUtils.fetchRecipes(this, URL, this);
    }
}
