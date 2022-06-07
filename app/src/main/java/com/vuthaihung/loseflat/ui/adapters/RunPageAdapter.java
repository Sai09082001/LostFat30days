package com.vuthaihung.loseflat.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vuthaihung.loseflat.data.model.WorkoutUser;
import com.vuthaihung.loseflat.ui.fragments.RunFragment;

import java.util.ArrayList;

public class RunPageAdapter extends FragmentPagerAdapter {
    private ArrayList<WorkoutUser> workoutUsers;

    public RunPageAdapter(FragmentManager fm, ArrayList<WorkoutUser> list) {
        super(fm);
        this.workoutUsers = list;
    }

    @Override
    public Fragment getItem(int position) {
        return RunFragment.newInstance(workoutUsers, workoutUsers.get(position), getCount(), position);
    }

    @Override
    public int getCount() {
        return workoutUsers.size();
    }
}
