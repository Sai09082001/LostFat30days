package com.lubuteam.sellsource.losefat.ui.base;

import android.os.Build;
import android.util.Log;

import com.ads.control.AdsApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import com.lubuteam.sellsource.losefat.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class BaseApplication extends AdsApplication {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        if (Build.VERSION.SDK_INT >= 21) {
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(getString(R.string.font_regular))
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        }

        instance = this;
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            Log.e("status", "application null");
        }
        return instance;
    }
}
