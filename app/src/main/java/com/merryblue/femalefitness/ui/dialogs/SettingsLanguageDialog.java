package com.merryblue.femalefitness.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.ui.base.BaseDialog;

public class SettingsLanguageDialog extends BaseDialog {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_settings_language, container, false);
        return rootView;
    }
}
