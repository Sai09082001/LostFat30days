package com.vuthaihung.loseflat.ui.fragments;

import static com.vuthaihung.loseflat.service.ApiService.gson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.ads.control.AdmobHelp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.AdmobFirebaseModel;
import com.vuthaihung.loseflat.ui.base.BaseFragment;
import com.vuthaihung.loseflat.utils.Utils;


public class AdsFragment extends BaseFragment {

    private int indexAdmob;

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
        indexAdmob = 0;
        if (Utils.isNetworkConnected(getContext())) {
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                onFirebaseRemoteSuccess();
                            } else {
                                // do nothing
                            }
                        }
                    });
        }

    }

    private void onFirebaseRemoteSuccess() {
        String admobId = mFirebaseRemoteConfig.getString("admob_workout_complete_native");
        AdmobFirebaseModel admobFirebaseModel = gson.fromJson(admobId, AdmobFirebaseModel.class);
        if (admobFirebaseModel.getStatus()){
            AdmobHelp.getInstance().loadNativeFragment(getActivity(),rootView,admobFirebaseModel.getListAdmob().get(indexAdmob) );
            if (indexAdmob >= admobFirebaseModel.getListAdmob().size()) indexAdmob = 0;
            else  indexAdmob++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
