package com.vuthaihung.loseflat.ui.activities;

import android.os.Bundle;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.ui.base.BaseActivity;

public class FeedBackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_feed_back);

        initViews();
    }

    private void initViews() {

    }
}
