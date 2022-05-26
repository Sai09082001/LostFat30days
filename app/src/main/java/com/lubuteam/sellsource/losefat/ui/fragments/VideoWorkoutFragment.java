package com.lubuteam.sellsource.losefat.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.Workout;
import com.lubuteam.sellsource.losefat.data.model.WorkoutUser;
import com.lubuteam.sellsource.losefat.ui.activities.viewmodel.RunViewModel;
import com.lubuteam.sellsource.losefat.ui.base.BaseFragment;
import com.lubuteam.sellsource.losefat.ui.base.MyViewModelFactory;
import com.lubuteam.sellsource.losefat.utils.Constants;
import com.lubuteam.sellsource.losefat.utils.ViewUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoWorkoutFragment extends BaseFragment implements YouTubePlayer.OnInitializedListener {

    private RunViewModel viewModel;

    private WorkoutUser workoutUser;
    private Workout workout;
    private boolean isVideo = true;
    private View tabVideo, tabAnimation;
    private View contentVideo, contentAnimation;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer player;


    public VideoWorkoutFragment(Object object) {
        if (object instanceof Workout) {
            workout = (Workout) object;
        } else {
            workoutUser = (WorkoutUser) object;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), MyViewModelFactory.getInstance()).get(RunViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video_workout, container, false);
        initViews();
        initEvents();
        initObservers();
        return rootView;
    }

    protected void initObservers() {

    }

    protected void initEvents() {
        tabVideo.setOnClickListener(view -> swap());
        tabAnimation.setOnClickListener(view -> swap());
        rootView.findViewById(R.id.btn_back).setOnClickListener(view -> {
            player.pause();
            viewModel.actionCloseHelp.call();
        });
    }

    protected void initViews() {

        tabVideo = rootView.findViewById(R.id.video);
        tabAnimation = rootView.findViewById(R.id.animation);
        contentVideo = rootView.findViewById(R.id.video_layout);
        contentAnimation = rootView.findViewById(R.id.animation_layout);

        if (workout == null) {
            ((TextView) rootView.findViewById(R.id.txt_title_anim)).setText(workoutUser.getData().getTitleDisplay() + " " + workoutUser.getTimeCountTitle(false));
            ((TextView) rootView.findViewById(R.id.txt_content_anim)).setText(workoutUser.getData().getDescriptionDisplay());
            ((TextView) rootView.findViewById(R.id.txt_title_video)).setText(workoutUser.getData().getTitleDisplay() + " " + workoutUser.getTimeCountTitle(false));
            ((TextView) rootView.findViewById(R.id.txt_content_video)).setText(workoutUser.getData().getDescriptionDisplay());
            if (workoutUser.getData().getType() == 0) {
                rootView.findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
                rootView.findViewById(R.id.txt_count_video).setVisibility(View.GONE);
            } else {
                ((TextView) rootView.findViewById(R.id.txt_count_anim)).setText("Each Side " + workoutUser.getTimeCountTitle(true));
                ((TextView) rootView.findViewById(R.id.txt_count_video)).setText("Each Side " + workoutUser.getTimeCountTitle(true));
            }
        } else {
            ((TextView) rootView.findViewById(R.id.txt_title_anim)).setText(workout.getTitleDisplay() + " " + workout.getTimeCountTitle(false));
            ((TextView) rootView.findViewById(R.id.txt_title_video)).setText(workout.getTitleDisplay() + " " + workout.getTimeCountTitle(false));
            ((TextView) rootView.findViewById(R.id.txt_content_anim)).setText(workout.getDescriptionDisplay());
            ((TextView) rootView.findViewById(R.id.txt_content_video)).setText(workout.getDescriptionDisplay());
            if (workout.getType() == 0) {
                rootView.findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
                rootView.findViewById(R.id.txt_count_video).setVisibility(View.GONE);
            } else {
                ((TextView) rootView.findViewById(R.id.txt_count_anim)).setText("Each Side " + workout.getTimeCountTitle(true));
                ((TextView) rootView.findViewById(R.id.txt_count_video)).setText("Each Side " + workout.getTimeCountTitle(true));
            }
        }

        ImageView imageView = rootView.findViewById(R.id.img_thumb);
        ViewUtils.bindImage(getContext(), ViewUtils.getPathWorkout(workout == null ? workoutUser.getData().getImageGender() : workout.getAnim()), imageView);

        youTubePlayerView = rootView.findViewById(R.id.video_player);
        youTubePlayerView.initialize(Constants.API_KEY, this);
    }

    private void swap() {
        isVideo = !isVideo;
        refresh();
    }

    private void refresh() {
        if (isVideo) {
            tabVideo.setVisibility(View.VISIBLE);
            tabAnimation.setVisibility(View.GONE);
            contentVideo.setVisibility(View.VISIBLE);
            contentAnimation.setVisibility(View.GONE);
        } else {
            if (player != null) {
                player.pause();
            }
            tabVideo.setVisibility(View.GONE);
            tabAnimation.setVisibility(View.VISIBLE);
            contentVideo.setVisibility(View.GONE);
            contentAnimation.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        if (!b) {
            this.player.loadVideo(workout == null ? workoutUser.getData().getVideoGender() : workout.getVideoGender());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getContext(), "Please Update Your Youtube Application", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
