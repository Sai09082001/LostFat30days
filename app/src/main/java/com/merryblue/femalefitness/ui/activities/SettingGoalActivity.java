package com.merryblue.femalefitness.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.shared.AppSettings;
import com.merryblue.femalefitness.ui.base.BaseActivity;
import com.merryblue.femalefitness.ui.dialogs.FirstDayOfWeekDialogFragment;
import com.merryblue.femalefitness.ui.dialogs.WeeklyDialogFragment;
import com.merryblue.femalefitness.ui.interfaces.DialogResultListener;

import java.util.Calendar;

public class SettingGoalActivity extends BaseActivity implements DialogResultListener {

    private int currentFirst;
    private int currentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_goal);


        initData();
        initViews();
        initEvents();
        refreshState();
    }

    private void refreshState() {
        String day = getResources().getString(R.string.day);
        String days = getResources().getString(R.string.days);
        String numberDays = currentNum + " " + (currentNum > 1 ? days : day);
        String firstDay = "";
        switch (currentFirst) {
            case Calendar.SUNDAY:
                firstDay = getResources().getString(R.string.sunday);
                break;
            case Calendar.MONDAY:
                firstDay = getResources().getString(R.string.monday);
                break;
            case Calendar.SATURDAY:
                firstDay = getResources().getString(R.string.saturday);
                break;
        }

        ((TextView) findViewById(R.id.txt_number_days_of_week)).setText(numberDays);
        ((TextView) findViewById(R.id.txt_first_day_of_week)).setText(firstDay);
    }

    private void initData() {
        currentFirst = AppSettings.getInstance().getFirstDayOfWeek();
        currentNum = AppSettings.getInstance().getNumberDaysOfWeekly();
    }

    private void initEvents() {
        findViewById(R.id.btn_weekly).setOnClickListener(view -> {
            new WeeklyDialogFragment(currentNum).show(getSupportFragmentManager(), null);
        });

        findViewById(R.id.btn_first_day).setOnClickListener(view -> {
            new FirstDayOfWeekDialogFragment(currentFirst).show(getSupportFragmentManager(), null);
        });

        findViewById(R.id.btn_save).setOnClickListener(view -> {
            AppSettings.getInstance().setFirstDayOfWeek(currentFirst);
            AppSettings.getInstance().setNumberDaysOfWeekly(currentNum);
            onBackPressed();
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        super.onBackPressed();
    }

    @Override
    public void onResult(int type, Object value) {
        if (type == DialogResultListener.FIRST_DAY_OF_WEEK) {
            currentFirst = (int) value;
        } else if (type == DialogResultListener.NUMBER_DAYS_WEEKLY) {
            currentNum = (int) value;
        }
        refreshState();
    }

}
