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
import com.squareup.picasso.Picasso;
import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedStepFragment extends Fragment {
    private static final String TAG = DetailedStepFragment.class.getSimpleName();
    private static final String STEP_KEY = "step";
    private static final String PLAYBACK_POSITION_KEY = "playback_key";
    private static final String CURRENT_POSITION_KEY = "current_position_key";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready_key";
    private static final String CONTAINS_VIDEO_KEY = "contains_video_key";

    @BindView(R.id.tv_step_desc)
    TextView stepDesTextView;
    @BindView(R.id.exoPlayerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_video_replacement)
    ImageView videoReplacementImageView;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private boolean containsVideo = false;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;

    public DetailedStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState restored");

            mStep = savedInstanceState.getParcelable(STEP_KEY);

            containsVideo = savedInstanceState.getBoolean(CONTAINS_VIDEO_KEY);

            if(containsVideo) {
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
                currentWindow = savedInstanceState.getInt(CURRENT_POSITION_KEY);
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            }
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
            containsVideo = false;

            mPlayerView.setVisibility(View.GONE);

            videoReplacementImageView.setVisibility(View.VISIBLE);

            if (mStep.getThumbnailURL() != null && ! mStep.getThumbnailURL().isEmpty()) {
                Picasso.with(getActivity())
                        .load(mStep.getThumbnailURL())
                        .placeholder(R.drawable.ic_video_replacement)
                        .error(R.drawable.ic_video_replacement)
                        .into(videoReplacementImageView);
            }
        } else {
            containsVideo = true;
        }

        stepDesTextView.setText(mStep.getDescription());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (containsVideo && Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (containsVideo && (Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (containsVideo && Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (containsVideo && Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STEP_KEY, mStep);
        outState.putBoolean(CONTAINS_VIDEO_KEY, containsVideo);

        if (containsVideo) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(PLAYBACK_POSITION_KEY, playbackPosition);

            currentWindow = mExoPlayer.getCurrentWindowIndex();
            outState.putInt(CURRENT_POSITION_KEY, currentWindow);

            playWhenReady = mExoPlayer.getPlayWhenReady();
            outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady);
        }

        Log.d(TAG, CONTAINS_VIDEO_KEY + "=" + containsVideo);
        Log.d(TAG, PLAYBACK_POSITION_KEY + "=" + playbackPosition);
        Log.d(TAG, CURRENT_POSITION_KEY + "=" + currentWindow);
        Log.d(TAG, PLAY_WHEN_READY_KEY + "=" + playWhenReady);
    }

    public void setStep(Step step) {
        Log.d(TAG, "Step is set");
        mStep = step;
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()),
                new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), getResources().getString(R.string.app_name))),
                new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(currentWindow, playbackPosition);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
