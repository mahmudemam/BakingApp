package com.udacity.nd.projects.bakingapp.ingredients;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {
    private static final String TAG = IngredientsFragment.class.getSimpleName();
    private static final String INGREDIENT_KEY = "ingredients";
    private static final String RV_POSITION_KEY = "position";
    @BindView(R.id.rv_ingredients)
    RecyclerView rv;
    private List<Ingredient> mIngredients;
    private View view;
    private Parcelable rvPosition;

    public IngredientsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState restored");

            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENT_KEY);
            rvPosition = savedInstanceState.getParcelable(RV_POSITION_KEY);
        }

        if (mIngredients == null) {
            Log.d(TAG, "onCreateView: mIngredients is null");
            throw new IllegalStateException("Ingredients are not set yet!");
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        setupRecyclerView(rv);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");

        outState.putParcelableArrayList(INGREDIENT_KEY, (ArrayList) mIngredients);
        if (rv != null) {
            outState.putParcelable(RV_POSITION_KEY, rv.getLayoutManager().onSaveInstanceState());
        }
    }

    public void setIngredients(List<Ingredient> ingredients) {
        Log.d(TAG, "setIngredients: count=" + ingredients.size());
        mIngredients = ingredients;
    }

    private void setupRecyclerView(RecyclerView rv) {
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.onRestoreInstanceState(rvPosition);

        rv.setLayoutManager(layoutManager);
        Log.d(TAG, "setupRecyclerView: setLayoutManager SUCCESS");

        rv.setAdapter(new IngredientAdapter(getActivity(), mIngredients));
        Log.d(TAG, "setupRecyclerView: setAdapter SUCCESS");
    }
}
