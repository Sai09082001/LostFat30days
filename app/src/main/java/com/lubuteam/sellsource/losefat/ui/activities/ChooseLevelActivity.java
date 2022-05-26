package com.lubuteam.sellsource.losefat.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.base.BaseActivity;

public class ChooseLevelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.btn_choose_1).setOnClickListener(view -> {
            callback(1);
        });
        findViewById(R.id.btn_choose_2).setOnClickListener(view -> {
            callback(2);
        });
        findViewById(R.id.btn_choose_3).setOnClickListener(view -> {
            callback(3);
        });
        findViewById(R.id.btn_choose_4).setOnClickListener(view -> {
            callback(4);
        });
    }

    private void callback(int pos) {
        AppSettings.getInstance().setLevel(pos);
        Intent intent = getIntent();
        intent.putExtra("data", pos);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int pos = AppSettings.getInstance().getLevel();
        AppCompatButton btn1, btn2, btn3, btn4;
        btn1 = findViewById(R.id.btn_choose_1);
        btn2 = findViewById(R.id.btn_choose_2);
        btn3 = findViewById(R.id.btn_choose_3);
        btn4 = findViewById(R.id.btn_choose_4);
        if (pos == 1) {
            btn1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (pos == 2) {
            btn2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (pos == 3) {
            btn3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (pos == 4) {
            btn4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
