package com.vuthaihung.loseflat.ui.activities;

import static com.vuthaihung.loseflat.service.ApiService.gson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.ads.control.AdmobHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.AdmobFirebaseModel;
import com.vuthaihung.loseflat.data.model.DailySectionUser;
import com.vuthaihung.loseflat.data.model.SectionUser;
import com.vuthaihung.loseflat.data.model.WorkoutUser;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.utils.Constants;
import com.vuthaihung.loseflat.utils.Utils;

import java.util.ArrayList;

public class LoadingInterAdActivity extends BaseActivity {

    private String admobStringId;
    private int indexAdmob;
    private AdmobFirebaseModel admobFirebaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_loading_inter_ad);
        indexAdmob=0;
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
//        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("KMFG", "onStart: admob ");
    }

    private void onFirebaseRemoteSuccess() {
        String admobId = mFirebaseRemoteConfig.getString("admob_workout_complete_back_interstital");
        admobFirebaseModel = gson.fromJson(admobId, AdmobFirebaseModel.class);
        if (admobFirebaseModel.getStatus() && admobFirebaseModel!=null){
            admobStringId = admobFirebaseModel.getListAdmob().get(indexAdmob);
            loadingInterAd();
        }
       // Log.i("KMFG", "onFirebaseRemoteSuccess: ok ");
    }

    private void initViews() {
       // loadingInterAd();
//        new Handler().postDelayed(this::, 2000);
    }

    private void loadingInterAd() {
        Intent intentAd = getIntent();
        String keyAd = intentAd.getStringExtra(Constants.KEY_LOADING_AD);
        AdmobHelp.getInstance().loadInterstitialAd(this,admobStringId);
        if (keyAd == null) return;
        switch (keyAd) {
            case "EditExerciseActivity":
            case "EditPlanActivity":
            case "ProfileActivity":
            case "ResultActivity02":
            case "RestDayActivity":
            case "RunActivity":
                AdmobHelp.getInstance().showInterstitialAd(this,null);
                break;
            case "ResultActivity01":
                AdmobHelp.getInstance().showInterstitialAd(this, () -> {
                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                });
            default:
                throw new IllegalStateException("Unexpected value: " + keyAd);
        }
        if (indexAdmob >= admobFirebaseModel.getListAdmob().size()) indexAdmob = 0;
        else  indexAdmob++;
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
