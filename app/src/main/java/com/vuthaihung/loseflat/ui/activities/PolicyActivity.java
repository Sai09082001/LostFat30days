package com.vuthaihung.loseflat.ui.activities;

import android.os.Bundle;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.ui.base.BaseActivity;

public class PolicyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.iv_back_policy).setOnClickListener(view -> {
            onBackPressed();
        });
    }
}
