package com.merryblue.femalefitness.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.ui.base.BaseFragment;

public class WeekTwoFragment extends BaseFragment {


    public WeekTwoFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_week_one, container, false);
        return rootView;
    }

}