package com.udacity.nd.projects.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.ingredients.IngredientsFragment;
import com.udacity.nd.projects.bakingapp.steps.DetailedStepActivity;
import com.udacity.nd.projects.bakingapp.steps.DetailedStepFragment;
import com.udacity.nd.projects.bakingapp.steps.StepsFragment;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsFragment.StepSelectedListener {
    public static final String RECIPE_KEY = "recipe";
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private static final String INGREDIENTS_FRAGMENT_KEY = "ingredient_fragment";
    private Recipe mRecipe;

    public static Intent getRecipeDetailsIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.RECIPE_KEY, recipe);

        return intent;
    }

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

        IngredientsFragment ingredientsFragment = (IngredientsFragment) fragmentManager.findFragmentByTag(INGREDIENTS_FRAGMENT_KEY);

        if (ingredientsFragment == null) {
            // Create Ingredients Fragment
            ingredientsFragment = new IngredientsFragment();
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
                    .add(R.id.ingredients_fragment_container, ingredientsFragment, INGREDIENTS_FRAGMENT_KEY)
                    .add(R.id.steps_fragment_container, stepsFragment, "steps_fragment")
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");

        outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    @Override
    public void onStepSelected(int stepId) {
        boolean isTwoPane = getResources().getBoolean(R.bool.isTwoPane);
        if (isTwoPane) {
            DetailedStepFragment detailedStepFragment = new DetailedStepFragment();
            detailedStepFragment.setStep(mRecipe.getSteps().get(stepId));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailed_step_fragment, detailedStepFragment)
                    .commit();
        } else {
            Intent detailedStepIntent = new Intent(this, DetailedStepActivity.class);
            detailedStepIntent.putExtra(DetailedStepActivity.STEP_ID_KEY, stepId);
            detailedStepIntent.putParcelableArrayListExtra(DetailedStepActivity.STEPS_KEY, (ArrayList<? extends Parcelable>) mRecipe.getSteps());
            detailedStepIntent.putExtra(DetailedStepActivity.RECIPE_NAME, mRecipe.getName());
            startActivity(detailedStepIntent);
        }
    }
}
