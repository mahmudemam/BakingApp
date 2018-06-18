package com.udacity.nd.projects.bakingapp.widgets;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.RecipeDetailsActivity;
import com.udacity.nd.projects.bakingapp.data.Ingredient;
import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;
import com.udacity.nd.projects.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by noname on 6/18/18.
 */

public class IngredientsListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = IngredientsListViewFactory.class.getSimpleName();
    private Context mContext;
    private Recipe mRecipe;
    private List<Ingredient> ingredients;

    IngredientsListViewFactory(Context context, Recipe recipe) {
        Log.d(TAG, "ctr");

        mContext = context;
        mRecipe = recipe;
        ingredients = mRecipe.getIngredients();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged");
    }

    @Override
    public int getCount() {
        Log.d(TAG, "count=" + ingredients.size());
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);

        Ingredient ingredient = ingredients.get(i);

        views.setTextViewText(R.id.appwidget_tv_ingredient, ingredient.getIngredient());
        views.setTextViewText(R.id.appwidget_tv_measure, ingredient.getQuantity() + " " + ingredient.getMeasure());

        Intent intent = RecipeDetailsActivity.getRecipeDetailsIntent(mContext, mRecipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_tv_ingredient, pendingIntent);

        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
