package com.vuthaihung.loseflat.ui.activities;

import static com.vuthaihung.loseflat.service.ApiService.gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.ads.control.AdmobHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vuthaihung.loseflat.BuildConfig;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.AdmobFirebaseModel;
import com.vuthaihung.loseflat.data.room.AppDatabase;
import com.vuthaihung.loseflat.data.room.AppDatabaseConst;
import com.vuthaihung.loseflat.data.shared.AppSettings;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.ui.base.BaseApplication;
import com.vuthaihung.loseflat.ui.interfaces.DatabaseListener;

import io.reactivex.schedulers.Schedulers;



public class SplashActivity extends BaseActivity implements DatabaseListener {

    private int timeout = 1000;
    ProgressBar pbLoadData;
    TextView tvStatus;
    Handler mHandler;
    Runnable r;
    private int indexAdmob;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);
        indexAdmob = 0;
//        LinearLayout lr = (LinearLayout) findViewById(R.id.ll_workout);
//        lr.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_to_right));
//        tvStatus = (TextView)findViewById(R.id.tv_slogan);
//        pbLoadData = (ProgressBar)findViewById(R.id.pbLoadData);
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
                        } else {
                            // do nothing
                        }
                    }
                });
        /**
         *  Important
         */
        if (BuildConfig.DB_VERSION > AppSettings.getInstance().getDBVersion()) {
            AppSettings.getInstance().setDbVersion(BuildConfig.DB_VERSION);

            // Clear and create const db
            AppDatabaseConst.clearInstance();
            AppDatabaseConst.copyAttachedDatabase(BaseApplication.getInstance(), true);
            AppDatabaseConst.getInstance()
                    .sectionDao().getAll()
                    .subscribeOn(Schedulers.single())
                    .observeOn(Schedulers.single())
                    .subscribe(response -> {
                        Log.e("status", "result: " + response.size());
                        AppDatabase.initAppDatabase(this);
                    }, Throwable::printStackTrace);

            // Init Database
            AppSettings.getInstance().setLastVersion(BuildConfig.VERSION_CODE);
        }else{
            mHandler = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    gotoHome();

                }
            };

            mHandler.postDelayed(r, timeout);

        }


    }

    private void onFirebaseRemoteSuccess() {
        String admobId = mFirebaseRemoteConfig.getString("admob_workout_complete_back_interstital");
        AdmobFirebaseModel admobFirebaseModel = gson.fromJson(admobId, AdmobFirebaseModel.class);
        AdmobHelp.getInstance().init(this,admobFirebaseModel.getListAdmob().get(indexAdmob));
        Log.i("KMFG", "onFirebaseRemoteSuccess: "+admobFirebaseModel.getListAdmob().get(indexAdmob));
    }

    private void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    private void gotoNext() {
        Intent intent = new Intent(this, OnBoardingActivity01.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
//    private void gotoGuide() {
//        Intent intent = new Intent(this, GuideGenderActivity.class);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//        finish();
//    }

    @Override
    public void insertAllCompleted() {
        if (AppSettings.getInstance().isFirstOpen()) {
            gotoNext();
        }else{
            gotoHome();
        }
    }
}
