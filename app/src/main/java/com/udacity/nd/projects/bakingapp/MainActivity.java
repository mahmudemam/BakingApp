package com.udacity.nd.projects.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;
import com.udacity.nd.projects.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NetworkUtils.FetchCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";
    private static final String RV_POSITION_KEY = "position";

    private List<Recipe> mRecipes;
    private Parcelable rvPosition;

    @BindView(R.id.rv_recipes)
    RecyclerView rv;

    @BindView(R.id.pb_loading_recipe)
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            loadingProgressBar.setVisibility(View.INVISIBLE);

            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            loadView();
        } else {
            final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

            NetworkUtils.fetchRecipes(this, URL, this);
        }
        /*Intent intent = new Intent(this, RecipeDetailsActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void onFetchResult(Object result) {
        try {
            loadingProgressBar.setVisibility(View.INVISIBLE);

            mRecipes = JsonUtils.parseRecipes((JSONArray) result);
            loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(RECIPES_KEY, (ArrayList) mRecipes);
        if (rv != null && rv.getLayoutManager() != null) {
            outState.putParcelable(RV_POSITION_KEY, rv.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        final String M = "onRestoreInstanceState: ";
        Log.d(TAG, M + "Start");
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPES_KEY)) {
                mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            }

            if (savedInstanceState.containsKey(RV_POSITION_KEY)) {
                rvPosition = savedInstanceState.getParcelable(RV_POSITION_KEY);
            }
        }
    }

    private void loadView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.onRestoreInstanceState(rvPosition);

        rv.setLayoutManager(layoutManager);

        rv.setAdapter(new RecipeAdapter(this, mRecipes));
    }
}
