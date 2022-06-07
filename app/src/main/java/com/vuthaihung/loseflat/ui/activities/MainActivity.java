package com.vuthaihung.loseflat.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.control.AdmobHelp;
import com.ads.control.Rate;
import com.roughike.bottombar.BottomBar;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.MessageEvent;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.ui.customViews.CustomViewPager;
import com.vuthaihung.loseflat.ui.fragments.ReportFragment;
import com.vuthaihung.loseflat.ui.fragments.SettingsFragment;
import com.vuthaihung.loseflat.ui.fragments.TrainingFragment;
import com.vuthaihung.loseflat.ui.fragments.WorkoutsFragment;
import com.vuthaihung.loseflat.utils.NotificationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends BaseActivity {
    private CustomViewPager viewPager;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
        initObservers();
        initEvents();
        AdmobHelp.getInstance().loadBanner(MainActivity.this );
    }

    private void initEvents() {

    }

    private void initObservers() {

    }

    private void initViews() {
        setStatusBarColor(getResources().getColor(R.color.white));
        initViewPager();
        initBottomMenu();

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) == 23 && calendar.get(Calendar.SECOND) == 0){
            NotificationUtils.showAlarmReminder(this, new Random().nextInt(Integer.MAX_VALUE));

            if (Calendar.getInstance().after(calendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(MainActivity.this, NotificationUtils.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
        }
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 30);
//        calendar.set(Calendar.SECOND, 0);

    }

    private void initViewPager() {
        viewPager = findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setStatusBarColor(getResources().getColor(R.color.white));
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomMenu() {
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_training:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tab_workouts:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.tab_report:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.tab_settings:
                    viewPager.setCurrentItem(3);
                    break;
            }
        });
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new TrainingFragment();
                case 1:
                    return new WorkoutsFragment();
                case 2:
                    return new ReportFragment();
                default:
                    return new SettingsFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getKey().equals(MessageEvent.OPEN_WORKOUT_EVENT)) {
            bottomBar.selectTabAtPosition(1, true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Rate.Show(this,1);
        //finishAffinity();
    }
}
