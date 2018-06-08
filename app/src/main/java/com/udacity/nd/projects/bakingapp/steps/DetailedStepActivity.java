package com.udacity.nd.projects.bakingapp.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedStepActivity extends AppCompatActivity {
    public static final String STEP_ID_KEY = "step_id";
    public static final String STEPS_KEY = "steps";

    @BindView(R.id.iv_goto_next)
    ImageView gotoNextImageView;

    @BindView(R.id.iv_goto_previous)
    ImageView gotoPreviousImageView;

    @BindView(R.id.tv_next_step)
    TextView nextStepTextView;

    @BindView(R.id.tv_prev_step)
    TextView prevStepTextView;

    private List<Step> mSteps;
    private int mStepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_step);

        if (savedInstanceState != null && savedInstanceState.containsKey(STEP_ID_KEY) && savedInstanceState.containsKey(STEPS_KEY)) {
            mStepId = savedInstanceState.getInt(STEP_ID_KEY);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_KEY);
        }

        if (mSteps == null) {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(STEP_ID_KEY) || !intent.hasExtra(STEPS_KEY)) {
                throw new IllegalArgumentException("intent or steps is not passed to the activity");
            }

            mStepId = intent.getIntExtra(STEP_ID_KEY, 0);
            mSteps = intent.getParcelableArrayListExtra(STEPS_KEY);
        }

        ButterKnife.bind(this);

        DetailedStepFragment stepFragment = new DetailedStepFragment();
        stepFragment.setStep(mSteps.get(mStepId));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.detailed_step_fragment, stepFragment)
                .commit();

        setupButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STEP_ID_KEY, mStepId);
        outState.putParcelableArrayList(STEPS_KEY, (ArrayList) mSteps);
    }

    private void setupButton() {
        setupNextVideo();

        setupPrevVideo();
    }

    private void setupNextVideo() {
        if (mStepId == (mSteps.size() - 1)) {
            gotoNextImageView.setVisibility(View.GONE);
            nextStepTextView.setVisibility(View.GONE);
        } else {
            gotoNextImageView.setVisibility(View.VISIBLE);
            nextStepTextView.setVisibility(View.VISIBLE);
            nextStepTextView.setText(mSteps.get(mStepId + 1).getShortDescription());
        }
    }

    private void setupPrevVideo() {
        if (mStepId == 0) {
            gotoPreviousImageView.setVisibility(View.GONE);
            prevStepTextView.setVisibility(View.GONE);
        } else {
            gotoPreviousImageView.setVisibility(View.VISIBLE);
            prevStepTextView.setVisibility(View.VISIBLE);
            prevStepTextView.setText(mSteps.get(mStepId - 1).getShortDescription());
        }
    }
}
