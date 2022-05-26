package com.lubuteam.sellsource.losefat.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.ui.base.BaseFragment;

public class WeekFourFragment extends BaseFragment {

    public WeekFourFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_week_one, container, false);
        return rootView;
    }

}
