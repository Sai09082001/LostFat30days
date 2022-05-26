package com.lubuteam.sellsource.losefat.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lubuteam.sellsource.losefat.data.model.WorkoutUser;
import com.lubuteam.sellsource.losefat.ui.fragments.RunFragment;

import java.util.ArrayList;

public class RunPageAdapter extends FragmentPagerAdapter {
    private ArrayList<WorkoutUser> workoutUsers;

    public RunPageAdapter(FragmentManager fm, ArrayList<WorkoutUser> list) {
        super(fm);
        this.workoutUsers = list;
    }

    @Override
    public Fragment getItem(int position) {
        return RunFragment.newInstance(workoutUsers.get(position), getCount(), position);
    }

    @Override
    public int getCount() {
        return workoutUsers.size();
    }
}
