package com.lubuteam.sellsource.losefat.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ads.control.AdmobHelp;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.ContainerVoiceEngine;
import com.lubuteam.sellsource.losefat.data.model.DailySectionUser;
import com.lubuteam.sellsource.losefat.data.model.DayHistoryModel;
import com.lubuteam.sellsource.losefat.data.model.MessageEvent;
import com.lubuteam.sellsource.losefat.data.model.SectionHistory;
import com.lubuteam.sellsource.losefat.data.model.SectionUser;
import com.lubuteam.sellsource.losefat.data.model.WorkoutUser;
import com.lubuteam.sellsource.losefat.data.repositories.DailySectionRepository;
import com.lubuteam.sellsource.losefat.data.repositories.DayHistoryRepository;
import com.lubuteam.sellsource.losefat.data.repositories.SectionHistoryRepository;
import com.lubuteam.sellsource.losefat.data.repositories.WorkoutRepository;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.activities.viewmodel.RunViewModel;
import com.lubuteam.sellsource.losefat.ui.adapters.RunPageAdapter;
import com.lubuteam.sellsource.losefat.ui.base.BaseActivity;
import com.lubuteam.sellsource.losefat.ui.base.MyViewModelFactory;
import com.lubuteam.sellsource.losefat.ui.customViews.StepProgressBarView;
import com.lubuteam.sellsource.losefat.ui.dialogs.QuitDialog;
import com.lubuteam.sellsource.losefat.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;


public class RunActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private RunViewModel viewModel;

    private SectionUser sectionUser;
    private ArrayList<WorkoutUser> workoutUsers;

    private StepProgressBarView stepProgress;
    private ViewPager viewPager;
    private RunPageAdapter adapter;

    private int resume = 0;
    private MediaPlayer mediaPlayer;

    // TTS
    private TextToSpeech tts;
    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        viewModel = ViewModelProviders.of(this, MyViewModelFactory.getInstance()).get(RunViewModel.class);

        sectionUser = getIntent().getParcelableExtra("section");
        workoutUsers = getIntent().getParcelableArrayListExtra("workouts");

        initTTS();
        initViews();
        initObservers();
        initEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resume > 0) {
            viewModel.actionResume.setValue(resume);
        }
        resume++;
        Log.e("status", "resume");
        startAudioBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.actionPause.setValue(resume);
        Log.e("status", "pause");
        pauseAudioBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getKey().equals(MessageEvent.STOP_AUDIO)) {
            stopSpeech();
        }
    }

    private void stopSpeech() {
        tts.stop();
    }

    private void pauseAudioBackground() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void startAudioBackground() {
        if (AppSettings.getInstance().isPlayAudioBackground()) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.play_background);
                mediaPlayer.setLooping(true);
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    private void initEvents() {

    }

    private void initTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            ready = false;
        }
        if (AppSettings.getInstance().getEngineDefault() == null) {
            tts = new TextToSpeech(this, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    String language = AppSettings.getInstance().getEngineLanguage();
                    Locale locale = Locale.ENGLISH;
                    if (!language.isEmpty()) {
                        locale = ContainerVoiceEngine.toLocal(language);
                    }
                    int result = tts.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("status", "speech not support");
                        ready = false;
                        Toast.makeText(RunActivity.this, "This language is not supported!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("status", "speech create success");
                        ready = true;
                        tts.setPitch(0.9f);
                        tts.setSpeechRate(1.0f);
                    }
                } else {
                    Log.e("status", "can not create speech");
                }
            });
        } else {
            tts = new TextToSpeech(this, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    String language = AppSettings.getInstance().getEngineLanguage();
                    Locale locale = Locale.ENGLISH;
                    if (!language.isEmpty()) {
                        locale = ContainerVoiceEngine.toLocal(language);
                    }
                    int result = tts.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("status", "speech not support");
                        ready = false;
                        Toast.makeText(RunActivity.this, "This language is not supported!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("status", "speech create success");
                        ready = true;
                        tts.setPitch(0.9f);
                        tts.setSpeechRate(1.0f);
                    }
                } else {
                    Log.e("status", "can not create speech");
                }
            }, AppSettings.getInstance().getEngineDefault().getPackageName());
        }
    }

    private void speech(String text) {
        Log.e("status", "speech: " + text);
        if (AppSettings.getInstance().getSound() && AppSettings.getInstance().getSoundVoice()) {
            if (tts != null && ready) {
                if (Build.VERSION.SDK_INT >= 21) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
                } else {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                Log.e("status", "speech null: " + ready);
            }
        }
    }

    private void initObservers() {
        viewModel.actionPrev.observe(this, aVoid -> {
            int value = viewModel.current.getValue();
            if (value > 0) {
                value--;
                viewPager.setCurrentItem(value);
            }
        });
        viewModel.actionNext.observe(this, aVoid -> {
            int value = viewModel.current.getValue();
            if (value < workoutUsers.size() - 1) {
                value++;
                viewPager.setCurrentItem(value);
            } else {
                gotoResult();
            }
        });
        viewModel.actionQuit.observe(this, aVoid -> saveData(sectionUser.getData().getType() == 1, false));
        viewModel.actionHelp.observe(this, aVoid -> {
            viewModel.actionPause.setValue(++resume);
            Intent intent = new Intent(RunActivity.this, VideoWorkoutActivity.class);
            intent.putExtra("data", workoutUsers.get(viewModel.current.getValue()));
            startActivity(intent);
//                findViewById(R.id.container).setVisibility(View.VISIBLE);
//                replaceFragment(new VideoWorkoutFragment(workoutUsers.get(viewModel.current.getValue())), R.id.container, "help", false, -1);
        });
        viewModel.actionCloseHelp.observe(this, aVoid -> {
//                Fragment helpFragment = getSupportFragmentManager().findFragmentByTag("help");
//                if (helpFragment != null) {
//                    getSupportFragmentManager().beginTransaction().remove(helpFragment).commitAllowingStateLoss();
//                }
//                viewModel.actionResume.setValue(++resume);
        });
        viewModel.actionBack.observe(this, aVoid -> backAction());
        viewModel.actionSpeech.observe(this, this::speech);
        viewModel.actionSwitchAudioBackground.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (AppSettings.getInstance().isPlayAudioBackground()) {
                    startAudioBackground();
                } else {
                    pauseAudioBackground();
                }
            }
        });
    }

    private void gotoResult() {
        saveData(sectionUser.getData().getType() == 1, true);
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        adapter = new RunPageAdapter(getSupportFragmentManager(), workoutUsers);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

        stepProgress = findViewById(R.id.step_progress);
        stepProgress.setup(workoutUsers.size());
        stepProgress.progress(0);

        mediaPlayer = MediaPlayer.create(this, R.raw.play_background);
        mediaPlayer.setLooping(true);
    }

    public void setCalories(int index, float calories) {
        workoutUsers.get(index).setCalories(calories);
    }

    @SuppressLint("CheckResult")
    private void saveData(boolean daily, boolean completedAll) {
        /***
         *  Finish workout
         */
        if (daily) {
            // Save Challenge
            DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
            if (completedAll) {
                dailySectionUser.setCompleted(true);
                dailySectionUser.setProgress(100f);
                DailySectionRepository.getInstance().update(dailySectionUser).subscribe(() -> {
                    Log.e("status", "update completed");
                    DailySectionRepository.getInstance().nextDaily(dailySectionUser);
                });
            } else if (!dailySectionUser.isCompleted()) {
                float current = viewModel.current.getValue();
                dailySectionUser.setProgress((current / dailySectionUser.getData().getData().getWorkoutsId().size()) * 100f);
                Log.e("status", dailySectionUser.getProgress() + "");
                DailySectionRepository.getInstance().update(dailySectionUser).subscribe(() -> {
                    Log.e("status", "update completed");
                });
            }
        }

        // Save history
        WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getWorkoutsId())
                .subscribe(response -> {
                    SectionHistory sectionHistory = new SectionHistory();
                    Calendar calendar = Calendar.getInstance();
                    sectionHistory.setId(calendar.getTime().getTime());
                    sectionHistory.setCalendar(calendar);

                    if (daily) {
                        DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
                        sectionHistory.setTitle(sectionUser.getTitleHistory(this, dailySectionUser.getLevel()));
                    } else {
                        sectionHistory.setTitle(sectionUser.getTitleHistory(this));
                    }

                    sectionHistory.setTotalTime(viewModel.countUpTimer.getTime() / 1000);
                    sectionHistory.setSectionId(sectionUser.getId());
                    sectionHistory.setThumb(sectionUser.getData().getThumb());
                    float totalCalories = 0;
                    for (int i = 0; i < response.size(); i++) {
                        if (i >= viewModel.current.getValue()) break;
                        WorkoutUser workoutUser = response.get(i);
                        if (workoutUser.getData().getType() == 0) {
                            totalCalories += workoutUser.getTotalCalories();
                        } else {
                            totalCalories += workoutUsers.get(i).getCalories();
                        }
                    }
                    if (totalCalories == 0) {
                        finish();
                        return;
                    }
                    sectionHistory.setCalories(totalCalories);
                    SectionHistoryRepository.getInstance().insert(sectionHistory).subscribe();

                    // Save DayHistory
                    boolean isUpdate = true;
                    DayHistoryModel dayHistoryModel = DayHistoryRepository.getInstance().getByIdWithoutObserve(DateUtils.getIdDay(calendar));
                    if (dayHistoryModel == null) {
                        isUpdate = false;
                        dayHistoryModel = new DayHistoryModel(calendar);
                    }
                    dayHistoryModel.addCalories(totalCalories);
                    dayHistoryModel.addExercise(viewModel.current.getValue() + 1);
                    if (isUpdate) {
                        DayHistoryRepository.getInstance().update(dayHistoryModel).subscribe();
                    } else {
                        DayHistoryRepository.getInstance().insert(dayHistoryModel).subscribe();
                    }

                    if (completedAll) {
                        // Goto result
                        Intent intent = new Intent(this, ResultActivity.class);
                        intent.putExtra("section", sectionUser);
                        intent.putExtra("workouts", workoutUsers);
                        intent.putExtra("exercises", response.size());
                        intent.putExtra("calories", totalCalories);
                        intent.putExtra("timer", viewModel.countUpTimer.getTime() / 1000);
                        if (daily) {
                            DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
                            intent.putExtra("challenge", dailySectionUser);
                        }
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                    finish();
                });
        /**
         *  Insert workout end
         */
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    private void backAction() {
        AdmobHelp.getInstance().showInterstitialAd(this, () -> new QuitDialog().show(getSupportFragmentManager(), null));


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewModel.current.setValue(position);
        stepProgress.progress(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
