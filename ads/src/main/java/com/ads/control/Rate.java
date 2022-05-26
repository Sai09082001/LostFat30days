package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;

import com.ads.control.funtion.UtilsApp;


public class Rate {
    public static void Show(final Context mContext, int Style) {
        Show(mContext, Style, true);
    }

    public static void Show(final Context mContext, int Style, boolean isFinish) {
        try {
            if (UtilsApp.isConnectionAvailable(mContext)) {
                if (!PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("Show_rate", false)) {
                    RateApp a = new RateApp(mContext, mContext.getString(R.string.email_feedback), mContext.getString(R.string.Title_email), Style, isFinish);
                    a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    a.show();
                } else if (isFinish) {
                    ((Activity) (mContext)).finish();
                }

            } else if (isFinish) {
                ((Activity) (mContext)).finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
            ((Activity) (mContext)).finish();
        }

    }
}
