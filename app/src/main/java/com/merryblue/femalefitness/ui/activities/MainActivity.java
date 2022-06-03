package com.merryblue.femalefitness.ui.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.control.AdmobHelp;
import com.ads.control.Rate;
import com.roughike.bottombar.BottomBar;
import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.model.MessageEvent;
import com.merryblue.femalefitness.ui.base.BaseActivity;
import com.merryblue.femalefitness.ui.customViews.CustomViewPager;
import com.merryblue.femalefitness.ui.fragments.ReportFragment;
import com.merryblue.femalefitness.ui.fragments.SettingsFragment;
import com.merryblue.femalefitness.ui.fragments.TrainingFragment;
import com.merryblue.femalefitness.ui.fragments.WorkoutsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



public class MainActivity extends BaseActivity {
    private CustomViewPager viewPager;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if (position == 0) {
                    setStatusBarColor(getResources().getColor(R.color.white));
                } else {
                    setStatusBarColor(Color.parseColor("#DDDDDD"));
                }
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
