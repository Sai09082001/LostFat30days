package com.lubuteam.sellsource.losefat.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ads.control.AdmobHelp;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.Workout;
import com.lubuteam.sellsource.losefat.data.model.WorkoutUser;
import com.lubuteam.sellsource.losefat.utils.Constants;
import com.lubuteam.sellsource.losefat.utils.ViewUtils;



public class VideoWorkoutActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private WorkoutUser workoutUser;
    private Workout workout;
    private boolean isVideo = false;
    private View tabVideo, tabAnimation;
    private View contentVideo, contentAnimation;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_workout);
        AdmobHelp.getInstance().loadNative(this);
        initViews();
        initEvents();
        initObservers();
        refresh();
    }

    private void initObservers() {

    }

    private void initEvents() {
        tabVideo.setOnClickListener(view -> swap());
        tabAnimation.setOnClickListener(view -> swap());
        findViewById(R.id.btn_back).setOnClickListener(view -> onBackPressed());
    }

    private void initViews() {
        Object object = getIntent().getParcelableExtra("data");
        if (object instanceof Workout) {
            workout = (Workout) object;
        } else {
            workoutUser = (WorkoutUser) object;
        }

        tabVideo = findViewById(R.id.video);
        tabAnimation = findViewById(R.id.animation);
        contentVideo = findViewById(R.id.video_layout);
        contentAnimation = findViewById(R.id.animation_layout);

        if (workout == null) {
            ((TextView) findViewById(R.id.txt_title_anim)).setText(workoutUser.getData().getTitleDisplay() + " " + workoutUser.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_content_anim)).setText(workoutUser.getData().getDescriptionDisplay());
            ((TextView) findViewById(R.id.txt_title_video)).setText(workoutUser.getData().getTitleDisplay() + " " + workoutUser.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_content_video)).setText(workoutUser.getData().getDescriptionDisplay());
            if (workoutUser.getData().getType() == 0) {
                findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
                findViewById(R.id.txt_count_video).setVisibility(View.GONE);
            } else {
                ((TextView) findViewById(R.id.txt_count_anim)).setText(getResources().getString(R.string.each_side) + " " + workoutUser.getTimeCountTitle(true));
                ((TextView) findViewById(R.id.txt_count_video)).setText(getResources().getString(R.string.each_side) + " " + workoutUser.getTimeCountTitle(true));
            }
        } else {
            ((TextView) findViewById(R.id.txt_title_anim)).setText(workout.getTitleDisplay() + " " + workout.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_title_video)).setText(workout.getTitleDisplay() + " " + workout.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_content_anim)).setText(workout.getDescriptionDisplay());
            ((TextView) findViewById(R.id.txt_content_video)).setText(workout.getDescriptionDisplay());
            if (workout.getType() == 0) {
                findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
                findViewById(R.id.txt_count_video).setVisibility(View.GONE);
            } else {
                ((TextView) findViewById(R.id.txt_count_anim)).setText(getResources().getString(R.string.each_side) + " " + workout.getTimeCountTitle(true));
                ((TextView) findViewById(R.id.txt_count_video)).setText(getResources().getString(R.string.each_side) + " " + workout.getTimeCountTitle(true));
            }
        }

        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathWorkout(workout == null ? workoutUser.getData().getImageGender() : workout.getAnim()), imageView);

        youTubePlayerView = findViewById(R.id.video_player);
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
        Toast.makeText(VideoWorkoutActivity.this, "Please Update Your Youtube Application", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
