package com.merryblue.femalefitness.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.merryblue.femalefitness.data.shared.AppSettings;
import com.merryblue.femalefitness.ui.base.BaseViewModel;
import com.merryblue.femalefitness.utils.Utils;

public class WaistlineDialogViewModel extends BaseViewModel {
    public MutableLiveData<Integer> unitType;
    public MutableLiveData<Float> waistline;


    public WaistlineDialogViewModel() {
        super();
        unitType = new MutableLiveData<>(AppSettings.getInstance().getUnitType());
        float value = AppSettings.getInstance().getWaistlineDefault();
        if (AppSettings.getInstance().getUnitType() == 1) {
            value = Utils.convertCmToFt(value);
        }
        waistline = new MutableLiveData<>(value);
    }
}
