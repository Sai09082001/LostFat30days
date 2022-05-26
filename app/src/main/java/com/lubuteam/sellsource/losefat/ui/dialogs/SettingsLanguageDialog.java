package com.lubuteam.sellsource.losefat.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.ui.base.BaseDialog;

public class SettingsLanguageDialog extends BaseDialog {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_settings_language, container, false);
        return rootView;
    }
}
