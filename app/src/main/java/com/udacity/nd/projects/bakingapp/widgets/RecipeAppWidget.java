package com.udacity.nd.projects.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.nd.projects.bakingapp.MainActivity;
import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.RecipeDetailsActivity;
import com.udacity.nd.projects.bakingapp.data.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        RemoteViews views = null;
        if (recipe != null) {
            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

            views.setTextViewText(R.id.appwidget_tv_recipe_name, recipe.getName());

            Intent listViewIntent = new Intent(context, IngredientsListViewService.class);
            listViewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            listViewIntent.putExtra(IngredientsListViewService.RECIPE_KEY, recipe);
            views.setRemoteAdapter(R.id.appwidget_list_ingredients, listViewIntent);
            views.setEmptyView(R.id.appwidget_list_ingredients, R.id.empty_view);

            Intent intent = RecipeDetailsActivity.getRecipeDetailsIntent(context, recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.appwidget_list_ingredients, pendingIntent);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_default_widget);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.appwidget_tv_default, pendingIntent);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startActionUpdateWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

