package com.ads.control;

import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;

public abstract class AdsApplication extends MultiDexApplication {

    private static AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                initializationStatus -> {
                });
        appOpenManager = new AppOpenManager(this);
    }

    protected abstract void onCreate(Bundle savedInstanceState);
}
