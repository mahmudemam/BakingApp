package com.udacity.nd.projects.bakingapp.steps;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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

    @BindView(R.id.exoPlayerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.iv_video_replacement)
    ImageView videoReplacementImageView;

    private SimpleExoPlayer mExoPlayer;

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

        if (mStep.getVideoURL() == null || mStep.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);

            videoReplacementImageView.setVisibility(View.VISIBLE);
        } else {
            setupVideo();
        }

        stepDesTextView.setText(mStep.getDescription());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
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

    private void setupVideo() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()),
                new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getResources().getString(R.string.app_name))),
                new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }
}
