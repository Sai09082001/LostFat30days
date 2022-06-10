package com.vuthaihung.loseflat.ui.activities;

import android.os.Bundle;
import android.view.View;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.ui.customViews.CustomScrollView;

public class PolicyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_back_policy).setOnClickListener(view -> {
            onBackPressed();
        });
        findViewById(R.id.tv_enable_scroll).setOnClickListener(view -> {
            findViewById(R.id.ln_enable_scroll).setVisibility(View.GONE);
            ((CustomScrollView)findViewById(R.id.sv_policy)).setScrollingEnabled(true);
        });
    }

    private void initViews() {
        ((CustomScrollView)findViewById(R.id.sv_policy)).setScrollingEnabled(false);
    }
}
