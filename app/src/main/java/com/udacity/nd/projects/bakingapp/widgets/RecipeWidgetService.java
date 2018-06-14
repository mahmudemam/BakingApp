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

public class RecipeWidgetService extends IntentService implements NetworkUtils.FetchCallback {
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

    @Override
    public void onFetchResult(Object result) {
        try {
            List<Recipe> recipes = JsonUtils.parseRecipes((JSONArray) result);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));

            int recipeId = getRecipeId(recipes);
            Recipe recipe = null;
            if (recipeId != -1)
                recipe = recipes.get(recipeId);

            RecipeAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, recipe);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void handleActionIngredientList() {
        final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        NetworkUtils.fetchRecipes(this, URL, this, null);
    }

    private int getRecipeId(List<Recipe> recipes) {
        Log.d(TAG, "getRecipeId: Started");

        String recipeName = null;
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        if (sharedPreferences != null && sharedPreferences.contains(getString(R.string.pref_favorite_key))) {
            recipeName = sharedPreferences.getString(getString(R.string.pref_favorite_key), null);
        }

        if (recipeName != null) {
            Log.d(TAG, "getRecipeId: recipeName=" + recipeName);
            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = recipes.get(i);

                if (recipeName.equals(recipe.getName()))
                    return i;
            }
        }

        Log.d(TAG, "getRecipeId: Started");
        return -1;
    }
}
