package com.vuthaihung.loseflat.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vuthaihung.loseflat.data.model.Reminder;
import com.vuthaihung.loseflat.data.repositories.ReminderRepository;
import com.vuthaihung.loseflat.data.room.AppDatabase;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("CheckResult")
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(AlarmUtils.ACTION_AUTO_START_ALARM)) {
                int id = intent.getIntExtra("id", 0);
                Log.e("status", "receive id: " + id);
                Reminder reminder = AppDatabase.getInstance().reminderDao().findByIdWithoutObserver(id);
                if (reminder != null) {
                    Calendar calendar = Calendar.getInstance();
                    if (reminder.hasToday()) {
                        Log.e("status", "show notify alarm");
                        NotificationUtils.showAlarmReminder(context, id);
                    } else {
                        Log.e("status", "not today");
                    }
                    Flowable.just(0).delay(70, TimeUnit.SECONDS)
                            .subscribe(response -> {
                                Log.e("status", "set alarm again");
                                reminder.setAlarm(context);
                            });

                    if (!reminder.isDisplay()) {
                        ReminderRepository.getInstance().delete(reminder);
                    }
                } else {
                    Log.e("status", "alarm null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
