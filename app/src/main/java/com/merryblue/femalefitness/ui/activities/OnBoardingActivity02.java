package com.merryblue.femalefitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.ui.base.BaseActivity;

public class OnBoardingActivity02 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_onboarding_02);
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_next_boarding).setOnClickListener(view -> {
            gotoNext();
        });
    }

    private void gotoNext() {
        Intent intent = new Intent(this, OnBoardingActivity03.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}