package com.udacity.nd.projects.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.nd.projects.bakingapp.data.Recipe;
import com.udacity.nd.projects.bakingapp.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noname on 6/2/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private Context mContext;
    private List<Recipe> mRecipes;
    private RecipeClickListener mListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeClickListener listener) {
        mContext = context;
        mRecipes = recipes;
        mListener = listener;
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

    public interface RecipeClickListener {
        void onRecipeClicked(Recipe recipe);

        void onFavoriteClicked(Recipe recipe, boolean isFavorite);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_name)
        TextView nameTextView;

        @BindView(R.id.tv_serving)
        TextView servingTextView;

        @BindView(R.id.tv_ingredients)
        TextView ingredientsTextView;

        @BindView(R.id.tv_steps)
        TextView stepsTextView;

        @BindView(R.id.ib_favorite)
        ImageButton favoriteImageButton;

        RecipeViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRecipeClicked((Recipe) view.getTag());
                }
            });

            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSelected = view.isSelected();

                    Log.d(TAG, "favoriteButton.isSelected=" + isSelected);

                    if (isSelected) {
                        favoriteImageButton.setImageResource(R.drawable.ic_favorite_border);
                        isSelected = false;
                    } else {
                        favoriteImageButton.setImageResource(R.drawable.ic_favorite);
                        isSelected = true;
                    }

                    view.setSelected(isSelected);
                    mListener.onFavoriteClicked(mRecipes.get(getAdapterPosition()), isSelected);
                }
            });
        }

        void bind(Recipe recipe) {
            itemView.setTag(recipe);

            nameTextView.setText(recipe.getName());
            servingTextView.setText(
                    mContext.getResources().getQuantityString(R.plurals.persons, recipe.getServings(), recipe.getServings()));

            ingredientsTextView.setText(
                    mContext.getResources().getQuantityString(R.plurals.ingredients, recipe.getIngredients().size(), recipe.getIngredients().size())
            );

            stepsTextView.setText(
                    mContext.getResources().getQuantityString(R.plurals.steps, recipe.getSteps().size(), recipe.getSteps().size())
            );

            if (isFavorite(recipe)) {
                favoriteImageButton.setImageResource(R.drawable.ic_favorite);
                favoriteImageButton.setSelected(true);
            } else {
                favoriteImageButton.setImageResource(R.drawable.ic_favorite_border);
                favoriteImageButton.setSelected(false);
            }
        }

        private boolean isFavorite(@NonNull Recipe recipe) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_key), mContext.MODE_PRIVATE);
            String recipeStr = sharedPreferences.getString(mContext.getResources().getString(R.string.pref_favorite_key), null);
            Log.v(TAG, "recipe=" + recipeStr);

            if (recipeStr == null)
                return false;

            try {
                Recipe favoriteRecipe = JsonUtils.toRecipe(recipeStr);

                return ((favoriteRecipe != null) && (recipe.getName().equals(favoriteRecipe.getName())));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());

                return false;
            }
        }
    }
}
