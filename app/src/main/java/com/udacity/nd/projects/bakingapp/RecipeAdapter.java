package com.udacity.nd.projects.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.udacity.nd.projects.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noname on 6/2/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.recipe_card_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        else return mRecipes.size();
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoView)
        VideoView videoView;

        @BindView(R.id.tv_recipe_name)
        TextView nameTextView;

        @BindView(R.id.tv_serving)
        TextView servingTextView;

        @BindView(R.id.tv_ingredients)
        TextView ingredientsTextView;

        @BindView(R.id.tv_steps)
        TextView stepsTextView;

        RecipeViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        void bind(Recipe recipe) {
            nameTextView.setText(recipe.getName());
            servingTextView.setText(String.valueOf(recipe.getServings()));
            ingredientsTextView.setText(String.valueOf(recipe.getIngredients().size()));
            stepsTextView.setText(String.valueOf(recipe.getSteps().size()));
        }
    }
}
