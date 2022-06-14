package com.vuthaihung.loseflat.ui.activities;

import static com.vuthaihung.loseflat.service.ApiService.gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ads.control.AdmobHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.AdmobFirebaseModel;
import com.vuthaihung.loseflat.data.model.Workout;
import com.vuthaihung.loseflat.data.model.WorkoutUser;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.ui.dialogs.viewmodel.EditWorkoutViewModel;
import com.vuthaihung.loseflat.utils.Constants;
import com.vuthaihung.loseflat.utils.Utils;
import com.vuthaihung.loseflat.utils.ViewUtils;



public class EditExerciseActivity extends BaseActivity {

    private static final String TAG_NAME = EditExerciseActivity.class.getSimpleName();
    private EditWorkoutViewModel viewModel;
    private WorkoutUser workoutUser;
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private int indexAdmob;
    private AdmobFirebaseModel admobFirebaseModel;
    private String admobStringId;
    private Handler repeatUpdateHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        setStatusBarColor(getResources().getColor(R.color.white));

        viewModel = new EditWorkoutViewModel();
        Object data = getIntent().getParcelableExtra("data");
        if (data instanceof WorkoutUser) {
            WorkoutUser workoutUser = (WorkoutUser) data;
            this.workoutUser = workoutUser;
            this.workoutUser.setReplaced(true);
        } else {
            Workout workout = (Workout) data;
            this.workoutUser = new WorkoutUser();
            this.workoutUser.setTime(workout.getTimeDefault());
            this.workoutUser.setCount(workout.getCountDefault());
            this.workoutUser.setData(workout);
            this.workoutUser.setCalories(workout.getCalories());
            this.workoutUser.setReplaced(true);
            this.workoutUser.setId(workout.getId());
            this.workoutUser.setTraining(workout.isTraining());
            this.workoutUser.setWorkoutUserId(workout.getId() + "-" + Utils.randomString(5));
        }

        initData();
        initViews();
        initEvents();
        initObserve();
    }

    protected void initData() {
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.setValue(workoutUser.getTime());
        } else {
            viewModel.counts.setValue(workoutUser.getCount());
        }

    }

    protected void initViews() {
        ((TextView) findViewById(R.id.txt_title)).setText(workoutUser.getData().getTitleDisplay());
        ((TextView) findViewById(R.id.txt_description)).setText(workoutUser.getData().getDescriptionDisplay());
        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
        ((TextView) findViewById(R.id.btn_save)).setText(getResources().getString(R.string.btn_save));
        handleLoadAdFirebase();
        if (workoutUser.getData().getType() == 1 && workoutUser.getData().isTwoSides()) {
            findViewById(R.id.txt_each_side).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txt_each_side).setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initEvents() {
        findViewById(R.id.btn_close).setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            onBackPressed();
        });
        findViewById(R.id.btn_increase).setOnClickListener(view -> increase());
        findViewById(R.id.btn_increase).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false;
            }
            return false;
        });
        findViewById(R.id.btn_increase).setOnLongClickListener(view -> {
            autoIncrement = true;
            repeatUpdateHandler.post(new EditExerciseActivity.RepetitiveUpdater());
            return false;
        });
        findViewById(R.id.btn_reduce).setOnClickListener(view -> reduce());
        findViewById(R.id.btn_reduce).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false;
            }
            return false;
        });
        findViewById(R.id.btn_reduce).setOnLongClickListener(view -> {
            autoDecrement = true;
            repeatUpdateHandler.post(new EditExerciseActivity.RepetitiveUpdater());
            return false;
        });
//        findViewById(R.id.btn_reset).setOnClickListener(view -> {
//            if (workoutUser.getData().getType() == 0) {
//                viewModel.seconds.setValue(workoutUser.getData().getTimeDefault());
//            } else {
//                viewModel.counts.setValue(workoutUser.getData().getCountDefault());
//            }
//
//        });
        findViewById(R.id.btn_save).setOnClickListener(view -> save());
        findViewById(R.id.img_cam).setOnClickListener(view -> {
            Intent intent = new Intent(this, VideoWorkoutActivity.class);
            intent.putExtra("data", workoutUser);
            startActivity(intent);
        });
        findViewById(R.id.txt_cam).setOnClickListener(view -> {
            Intent intent = new Intent(this, VideoWorkoutActivity.class);
            intent.putExtra("data", workoutUser);
            startActivity(intent);
        });
    }

    private void callback() {
        Intent intent = getIntent();
        intent.putExtra("data", workoutUser);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void save() {
        if (workoutUser.getData().getType() == 0) {
            workoutUser.setTime(viewModel.seconds.getValue());
        } else {
            workoutUser.setCount(viewModel.counts.getValue());
        }
        callback();
//        addDisposable(WorkoutRepository.getInstance().insertWorkoutUser(workoutUser).subscribe(this::callback));
    }

    private void increase() {
        if (workoutUser.getData().getType() == 0) {
            int current = viewModel.seconds.getValue();
            if (current >= 3598) {
                current = 3598;
            }
            viewModel.seconds.setValue(++current);
        } else {
            int current = viewModel.counts.getValue();
            if (current >= 199) {
                current = 199;
            }
            viewModel.counts.setValue(++current);
        }
    }

    private void reduce() {
        if (workoutUser.getData().getType() == 0) {
            int current = viewModel.seconds.getValue();
            if (current <= 2) {
                current = 2;
            }
            viewModel.seconds.setValue(--current);
        } else {
            int current = viewModel.counts.getValue();
            if (current <= 2) {
                current = 2;
            }
            viewModel.counts.setValue(--current);
        }
    }

    protected void initObserve() {
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.observe(this, integer -> ((AppCompatTextView) findViewById(R.id.txt_time)).setText(Utils.convertStringTime(integer)));
        } else {
            viewModel.counts.observe(this, integer -> ((AppCompatTextView) findViewById(R.id.txt_time)).setText("" + integer));
        }
    }

    class RepetitiveUpdater implements Runnable {

        @Override
        public void run() {
            if (autoIncrement) {
                increase();
                repeatUpdateHandler.postDelayed(new EditExerciseActivity.RepetitiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                reduce();
                repeatUpdateHandler.postDelayed(new EditExerciseActivity.RepetitiveUpdater(), REPEAT_DELAY);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if ((AdmobHelp.getInstance().getTimeLoad() + AdmobHelp.getInstance().getTimeReload()) < System.currentTimeMillis()) {
            AdmobHelp.getInstance().loadInterstitialAd(this , admobStringId);
            if (AdmobHelp.getInstance().canShowInterstitialAd(this)) {
                Intent intentAd = new Intent(this, LoadingInterAdActivity.class);
                intentAd.putExtra(Constants.KEY_LOADING_AD, TAG_NAME);
                startActivity(intentAd);
            }
        }
        finish();
    }

    private void handleLoadAdFirebase() {
        if (Utils.isNetworkConnected(this)){
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                onFirebaseRemoteSuccess();
                                Log.i("KMFG", "onFirebaseRemoteSuccess: ok ");
                            } else {
                                // do nothing
                            }
                        }
                    });
        }
    }

    private void onFirebaseRemoteSuccess() {
        String admobId = mFirebaseRemoteConfig.getString("admob_workout_complete_back_interstital");
        admobFirebaseModel = gson.fromJson(admobId, AdmobFirebaseModel.class);
        if (admobFirebaseModel.getStatus() && admobFirebaseModel!=null) {
            admobStringId = admobFirebaseModel.getListAdmob().get(indexAdmob);
        }
    }
}

