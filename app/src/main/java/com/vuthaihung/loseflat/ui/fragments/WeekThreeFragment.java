package com.vuthaihung.loseflat.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.ui.base.BaseFragment;

public class WeekThreeFragment extends BaseFragment {


    public WeekThreeFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_week_one, container, false);
        return rootView;
    }

}