package com.udacity.nd.projects.bakingapp.steps;

import android.content.Context;
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
import com.udacity.nd.projects.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment {
    private static final String TAG = StepsFragment.class.getSimpleName();
    private static final String STEPS_KEY = "steps";
    private static final String RV_POSITION_KEY = "position";
    @BindView(R.id.rv_ingredients)
    RecyclerView rv;
    private List<Step> mSteps;
    private StepSelectedListener mListener;
    private View view;
    private Parcelable rvPosition;

    public StepsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof StepSelectedListener))
            throw new IllegalStateException("context is not an instanceof StepSelectedListener");

        mListener = (StepSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState restored");

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

        Log.d(TAG, "onSaveInstanceState");

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
        rv.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.onRestoreInstanceState(rvPosition);

        rv.setLayoutManager(layoutManager);
        Log.d(TAG, "setupRecyclerView: setLayoutManager SUCCESS");

        rv.setAdapter(new StepAdapter(getActivity(), mSteps, new StepAdapter.StepSelectedListener() {
            @Override
            public void onStepSelected(int stepId) {
                mListener.onStepSelected(stepId);
            }
        }));
        Log.d(TAG, "setupRecyclerView: setAdapter SUCCESS");
    }

    public interface StepSelectedListener {
        void onStepSelected(int stepId);
    }
}
