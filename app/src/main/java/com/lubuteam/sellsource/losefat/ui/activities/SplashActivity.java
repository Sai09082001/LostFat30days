package com.lubuteam.sellsource.losefat.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ads.control.AdmobHelp;
import com.lubuteam.sellsource.losefat.BuildConfig;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.room.AppDatabase;
import com.lubuteam.sellsource.losefat.data.room.AppDatabaseConst;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.base.BaseActivity;
import com.lubuteam.sellsource.losefat.ui.base.BaseApplication;
import com.lubuteam.sellsource.losefat.ui.interfaces.DatabaseListener;

import io.reactivex.schedulers.Schedulers;



public class SplashActivity extends BaseActivity implements DatabaseListener {

    private int timeout = 3000;
    ProgressBar pbLoadData;
    TextView tvStatus;
    Handler mHandler;
    Runnable r;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);
        LinearLayout lr = (LinearLayout) findViewById(R.id.ll_workout);
        lr.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_to_right));
        tvStatus = (TextView)findViewById(R.id.tv_slogan);
        pbLoadData = (ProgressBar)findViewById(R.id.pbLoadData);
        AdmobHelp.getInstance().init(this);
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

    private void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    private void gotoNext() {
        Intent intent = new Intent(this, GuideLevelActivity.class);
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
