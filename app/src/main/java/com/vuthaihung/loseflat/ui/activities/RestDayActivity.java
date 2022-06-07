package com.vuthaihung.loseflat.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.ads.control.AdmobHelp;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.DailySectionUser;
import com.vuthaihung.loseflat.data.repositories.DailySectionRepository;
import com.vuthaihung.loseflat.ui.base.BaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class RestDayActivity extends BaseActivity {

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
        AdmobHelp.getInstance().showInterstitialAd(this, () -> finish());
    }
}
