package com.vuthaihung.loseflat.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.ui.base.BaseActivity;

public class OnBoardingActivity03 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_onboarding_03);
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_next_boarding).setOnClickListener(view -> {
            gotoNext();
        });
    }

    private void gotoNext() {
        Intent intent = new Intent(this, GuideLevelActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}