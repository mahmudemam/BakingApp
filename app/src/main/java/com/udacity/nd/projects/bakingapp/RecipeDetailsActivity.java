package com.udacity.nd.projects.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.ingredients.IngredientsFragment;
import com.udacity.nd.projects.bakingapp.steps.StepsFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    public static final String RECIPE_KEY = "recipe";
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)) {
            Log.d(TAG, "onCreate: recipe restored");

            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        if (mRecipe == null) {
            Log.d(TAG, "onCreate: New creation");

            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(RECIPE_KEY)) {
                Log.e(TAG, "Recipe is not passed");
                finish();
                return;
            }

            mRecipe = intent.getParcelableExtra(RECIPE_KEY);
        }

        setContentView(R.layout.activity_recipe_details);
        setTitle(mRecipe.getName());

        setupFragments();
    }

    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Create Ingredients Fragment
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Log.d(TAG, "IngredientsFragment created");

        ingredientsFragment.setIngredients(mRecipe.getIngredients());
        Log.d(TAG, "Ingredient list added to IngredientsFragment");

        // Create Steps Fragment
        StepsFragment stepsFragment = new StepsFragment();
        Log.d(TAG, "StepsFragment created");

        stepsFragment.setSteps(mRecipe.getSteps());
        Log.d(TAG, "Step list added to StepsFragment");

        // Setup fragments
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_fragment_container, ingredientsFragment)
                .add(R.id.steps_fragment_container, stepsFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");

        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
