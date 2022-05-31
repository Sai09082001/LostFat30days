package com.merryblue.femalefitness.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.merryblue.femalefitness.data.shared.AppSettings;
import com.merryblue.femalefitness.ui.base.BaseViewModel;

public class RestSetViewModel extends BaseViewModel {
    public MutableLiveData<Integer> seconds;

    public RestSetViewModel(boolean isRestSet) {
        super();
        int value;
        if (isRestSet) {
            value = AppSettings.getInstance().getRestSet();
        } else {
            value = AppSettings.getInstance().getCountDown();
        }
        seconds = new MutableLiveData<>(value);
    }
}
