package com.merryblue.femalefitness.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.model.Reminder;
import com.merryblue.femalefitness.data.repositories.ReminderRepository;
import com.merryblue.femalefitness.ui.adapters.ReminderAdapter;
import com.merryblue.femalefitness.ui.adapters.decoration.ReminderDecoration;
import com.merryblue.femalefitness.ui.base.BaseActivity;
import com.merryblue.femalefitness.ui.dialogs.ReminderRepeatPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ReminderActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminders = new ArrayList<>();
    private Reminder newReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_reminder);
        initViews();
        initEvents();
        loadData();
    }

    private void initEvents() {
        findViewById(R.id.btn_add).setOnClickListener(view -> addReminder());
    }

    private void addReminder() {
        // Show TimePicker
        Calendar calendar = Calendar.getInstance();
        Dialog dialog = new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void loadData() {
        addDisposable(ReminderRepository.getInstance().getAll().subscribe(response -> {
            Collections.sort(response);
            reminders.clear();
            reminders.addAll(response);
            adapter.notifyDataSetChanged();
        }));
    }

    private void initViews() {
//        Toolbar toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        findViewById(R.id.iv_back_reminder).setOnClickListener(view -> {
            onBackPressed();
        });
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ReminderDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReminderAdapter(getSupportFragmentManager(), reminders);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        new ReminderRepeatPicker(i, i1).show(getSupportFragmentManager(), null);
    }
}
