package com.udacity.nd.projects.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;
import com.udacity.nd.projects.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkUtils.FetchCallback {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_card_view);

        final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        Log.v(TAG, URL);

        NetworkUtils.fetchRecipes(this, URL, this);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        startActivity(intent);

        //findViewById(R.id.videoView);
    }

    @Override
    public void onFetchResult(Object result) {
        try {
            List<Recipe> recipes = JsonUtils.parseRecipes((JSONArray) result);
            Log.d(TAG, recipes.size() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
