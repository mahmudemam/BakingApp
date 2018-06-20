package com.udacity.nd.projects.bakingapp.widgets;

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

import java.io.IOException;
import java.util.List;

public class IngredientsListViewService extends RemoteViewsService {
    public static final String RECIPE_KEY = "recipe";
    private static final String TAG = IngredientsListViewService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory, contains recipe=" + intent.hasExtra(RECIPE_KEY));
        return new IngredientsListViewFactory();
    }

    class IngredientsListViewFactory implements RemoteViewsService.RemoteViewsFactory {
        private Recipe mRecipe;
        private List<Ingredient> ingredients;

        IngredientsListViewFactory() {
            Log.d(TAG, "ctr");
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

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
            if (sharedPreferences != null && sharedPreferences.contains(getString(R.string.pref_favorite_key))) {
                String recipeStr = sharedPreferences.getString(getString(R.string.pref_favorite_key), null);

                if (recipeStr == null)
                    return;

                Log.v(TAG, "recipeStr=" + recipeStr);

                mRecipe = null;
                try {
                    mRecipe = JsonUtils.toRecipe(recipeStr);
                    if (mRecipe != null)
                        ingredients = mRecipe.getIngredients();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        @Override
        public int getCount() {
            if (ingredients == null) return 0;

            Log.d(TAG, "count=" + ingredients.size());
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Ingredient ingredient = ingredients.get(i);

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_ingredient_list_item);

            views.setTextViewText(R.id.appwidget_tv_ingredient, ingredient.getIngredient());
            views.setTextViewText(R.id.appwidget_tv_measure, ingredient.getQuantity() + " " + ingredient.getMeasure());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(RecipeDetailsActivity.RECIPE_KEY, mRecipe);
            views.setOnClickFillInIntent(R.id.appwidget_tv_ingredient, fillInIntent);

            return views;
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

}
