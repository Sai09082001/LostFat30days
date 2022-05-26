package com.lubuteam.sellsource.losefat.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.TipsArticle;
import com.lubuteam.sellsource.losefat.ui.base.BaseActivity;
import com.lubuteam.sellsource.losefat.utils.ViewUtils;

public class TipsDetailActivity extends BaseActivity {
    private TipsArticle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);
        data = getIntent().getParcelableExtra("data");
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        ((TextView) findViewById(R.id.txt_title)).setText(data.getTitle());
        ((TextView) findViewById(R.id.txt_content)).setText(data.getContent());
        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathSection(data.getImage()), imageView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
