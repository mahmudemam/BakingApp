package com.udacity.nd.projects.bakingapp.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedStepFragment extends Fragment {
    private static final String TAG = DetailedStepFragment.class.getSimpleName();
    private Step mStep;
    private static final String STEP_KEY = "step";

    @BindView(R.id.tv_step_desc)
    TextView stepDesTextView;

    @BindView(R.id.vv_step_video)
    VideoView stepVideoView;

    public DetailedStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState restored");

            mStep = savedInstanceState.getParcelable(STEP_KEY);
        }

        if (mStep == null) {
            Log.d(TAG, "onCreateView: mStep is null");
            throw new IllegalStateException("mStep are not set yet!");
        }

        View view = inflater.inflate(R.layout.fragment_detailed_step, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepDesTextView.setText(mStep.getDescription());
    }

    public void setStep(Step step) {
        Log.d(TAG, "Step is set");
        mStep = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STEP_KEY, mStep);
    }
}
