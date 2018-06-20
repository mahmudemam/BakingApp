package com.udacity.nd.projects.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;
import com.udacity.nd.projects.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by noname on 6/9/18.
 */

public class RecipeWidgetService extends IntentService {
    public static final String ACTION_INGREDIENT_LIST = "ingredients";
    private static final String TAG = RecipeWidgetService.class.getSimpleName();

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

    private void handleActionIngredientList() {
        Recipe favoriteRecipe = null;

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        if (sharedPreferences != null && sharedPreferences.contains(getString(R.string.pref_favorite_key))) {
            String recipeStr = sharedPreferences.getString(getString(R.string.pref_favorite_key), null);

            if (recipeStr != null) {
                Log.v(TAG, "recipeStr=" + recipeStr);

                try {
                    favoriteRecipe = JsonUtils.toRecipe(recipeStr);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        Log.d(TAG, "favoriteRecipe=" + (favoriteRecipe == null ? "null" : "exists"));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_ingredients);
        RecipeAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, favoriteRecipe);
    }
}
