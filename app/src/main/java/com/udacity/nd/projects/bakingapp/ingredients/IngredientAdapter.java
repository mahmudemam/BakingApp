package com.udacity.nd.projects.bakingapp.ingredients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private static final String TAG = IngredientAdapter.class.getSimpleName();
    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientAdapter(@NonNull Context context, @NonNull List<Ingredient> ingredients) {
        this.mContext = context;
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_view_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position=" + position);
        holder.bind(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        else return mIngredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_name)
        TextView name;

        @BindView(R.id.tv_ingredient_qty)
        TextView qty;

        @BindView(R.id.tv_ingredient_measure)
        TextView measure;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(Ingredient ingredient) {
            name.setText(ingredient.getIngredient());
            qty.setText(String.valueOf(ingredient.getQuantity()));
            measure.setText(ingredient.getMeasure());
        }
    }
}
