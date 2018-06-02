package com.udacity.nd.projects.bakingapp.steps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.nd.projects.bakingapp.R;
import com.udacity.nd.projects.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noname on 6/2/18.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context mContext;
    private List<Step> mSteps;

    public StepAdapter(Context context, List<Step> steps) {
        mContext = context;
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.step_view_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        else return mSteps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_short_desc)
        TextView shortDescTextView;

        @BindView(R.id.tv_step_desc)
        TextView descTextView;

        @BindView(R.id.ib_play_video)
        ImageButton playImageButton;

        StepViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        void bind(Step step) {
            shortDescTextView.setText(step.getShortDescription());
            descTextView.setText(step.getDescription());
            if (step.getVideoURL() == null) {
                playImageButton.setVisibility(View.INVISIBLE);
            }
        }
    }
}
