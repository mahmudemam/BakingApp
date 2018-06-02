package com.udacity.nd.projects.bakingapp.utils;

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
import com.udacity.nd.projects.bakingapp.data.Step;
import com.udacity.nd.projects.bakingapp.ingredients.IngredientAdapter;
import com.udacity.nd.projects.bakingapp.steps.StepAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment {
    private List<Step> mSteps;
    private static final String TAG = StepsFragment.class.getSimpleName();
    private static final String STEPS_KEY = "steps";
    private static final String RV_POSITION_KEY = "position";

    private View view;
    private Parcelable rvPosition;
    @BindView(R.id.rv_ingredients)
    RecyclerView rv;


    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_KEY);
            rvPosition = savedInstanceState.getParcelable(RV_POSITION_KEY);
        }

        if (mSteps == null) {
            Log.d(TAG, "onCreateView: mSteps is null");
            throw new IllegalStateException("mSteps are not set yet!");
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_steps, container, false);

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

        outState.putParcelableArrayList(STEPS_KEY, (ArrayList) mSteps);
        if (rv != null) {
            outState.putParcelable(RV_POSITION_KEY, rv.getLayoutManager().onSaveInstanceState());
        }
    }

    public void setSteps(List<Step> steps) {
        Log.d(TAG, "setSteps: count=" + steps.size());
        mSteps = steps;
    }

    private void setupRecyclerView(RecyclerView rv) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.onRestoreInstanceState(rvPosition);

        rv.setLayoutManager(layoutManager);
        Log.d(TAG, "setupRecyclerView: setLayoutManager SUCCESS");

        rv.setAdapter(new StepAdapter(getActivity(), mSteps));
        Log.d(TAG, "setupRecyclerView: setAdapter SUCCESS");
    }
}
