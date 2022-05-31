package com.merryblue.femalefitness.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.merryblue.femalefitness.data.shared.AppSettings;
import com.merryblue.femalefitness.ui.base.BaseViewModel;
import com.merryblue.femalefitness.utils.Utils;

public class WeightDialogViewModel extends BaseViewModel {
    public MutableLiveData<Integer> unitType;
    public MutableLiveData<Float> weight;


    public WeightDialogViewModel() {
        super();
        unitType = new MutableLiveData<>(AppSettings.getInstance().getUnitType());
        float value = AppSettings.getInstance().getWeightDefault();
        if (AppSettings.getInstance().getUnitType() == 1) {
            value = Utils.convertKgToLbs(value);
        }
        weight = new MutableLiveData<>(value);
    }
}
