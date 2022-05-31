package com.merryblue.femalefitness.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.ui.base.BaseFragment;



public class AdsSmallFragment extends BaseFragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ads_small, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

      //  AdsCompat.getInstance(getActivity()).loadNativeBannerFragment(rootView,true);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
