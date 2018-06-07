package com.udacity.nd.projects.bakingapp.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedStepActivity extends AppCompatActivity {
    public static final String STEP_KEY = "step";
    public static final String NEXT_STEP_KEY = "next_step";
    public static final String PREV_STEP_KEY = "prev_step";

    @BindView(R.id.iv_goto_next)
    ImageView gotoNextImageView;

    @BindView(R.id.iv_goto_previous)
    ImageView gotoPreviousImageView;

    @BindView(R.id.tv_next_step)
    TextView nextStepTextView;

    @BindView(R.id.tv_prev_step)
    TextView prevStepTextView;

    private Step mStep;
    private String mNextStep;
    private String mPrevStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_step);

        if (savedInstanceState != null && savedInstanceState.containsKey(STEP_KEY)) {
            mStep = savedInstanceState.getParcelable(STEP_KEY);

            if (savedInstanceState.containsKey(NEXT_STEP_KEY))
                mNextStep = savedInstanceState.getString(NEXT_STEP_KEY);

            if (savedInstanceState.containsKey(PREV_STEP_KEY))
                mPrevStep = savedInstanceState.getString(PREV_STEP_KEY);
        }

        if (mStep == null) {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(STEP_KEY)) {
                throw new IllegalArgumentException("intent or step is not passed to the activity");
            }

            mStep = intent.getParcelableExtra(STEP_KEY);

            if (intent.hasExtra(NEXT_STEP_KEY))
                mNextStep = intent.getStringExtra(NEXT_STEP_KEY);

            if (intent.hasExtra(PREV_STEP_KEY))
                mPrevStep = intent.getStringExtra(PREV_STEP_KEY);
        }

        ButterKnife.bind(this);

        DetailedStepFragment stepFragment = new DetailedStepFragment();
        stepFragment.setStep(mStep);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.detailed_step_fragment, stepFragment)
                .commit();

        setupButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STEP_KEY, mStep);
        if (mNextStep != null)
            outState.putString(NEXT_STEP_KEY, mNextStep);
        if (mNextStep != null)
            outState.putString(PREV_STEP_KEY, mPrevStep);
    }

    void setupButton() {
        if (mNextStep == null) {
            gotoNextImageView.setVisibility(View.GONE);
            nextStepTextView.setVisibility(View.GONE);
        } else {
            gotoNextImageView.setVisibility(View.VISIBLE);
            nextStepTextView.setVisibility(View.VISIBLE);
            nextStepTextView.setText(mNextStep);
        }

        if (mPrevStep == null) {
            gotoPreviousImageView.setVisibility(View.GONE);
            prevStepTextView.setVisibility(View.GONE);
        } else {
            gotoPreviousImageView.setVisibility(View.VISIBLE);
            prevStepTextView.setVisibility(View.VISIBLE);
            prevStepTextView.setText(mPrevStep);
        }
    }
}
