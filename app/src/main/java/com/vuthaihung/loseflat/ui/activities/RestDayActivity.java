package com.vuthaihung.loseflat.ui.activities;

import static com.vuthaihung.loseflat.service.ApiService.gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;


import com.ads.control.AdmobHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.AdmobFirebaseModel;
import com.vuthaihung.loseflat.data.model.DailySectionUser;
import com.vuthaihung.loseflat.data.repositories.DailySectionRepository;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.utils.Constants;
import com.vuthaihung.loseflat.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class RestDayActivity extends BaseActivity {

    private static final String TAG_NAME = RestDayActivity.class.getSimpleName();
    private int indexAdmob;
    private AdmobFirebaseModel admobFirebaseModel;
    private String admobStringId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_day);
        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
    }

    @SuppressLint("CheckResult")
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleLoadAdFirebase();
        findViewById(R.id.btn_ok).setOnClickListener(view -> {
            DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
            if (dailySectionUser.isCompleted()) {
                onBackPressed();
            } else {
                dailySectionUser.setCompleted(true);
                dailySectionUser.setProgress(100f);
                DailySectionRepository.getInstance().update(dailySectionUser)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            DailySectionRepository.getInstance().nextDaily(dailySectionUser);
                            onBackPressed();
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
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
       // AdmobHelp.getInstance().showInterstitialAd(this, () -> finish());
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
