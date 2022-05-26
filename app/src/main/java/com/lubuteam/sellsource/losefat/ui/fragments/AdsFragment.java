package com.lubuteam.sellsource.losefat.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.ads.control.AdmobHelp;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.ui.base.BaseFragment;



public class AdsFragment extends BaseFragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ads, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        AdmobHelp.getInstance().loadNativeFragment(getActivity(),rootView );

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
