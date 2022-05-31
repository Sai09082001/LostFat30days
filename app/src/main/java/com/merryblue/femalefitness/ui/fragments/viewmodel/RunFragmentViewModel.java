package com.merryblue.femalefitness.ui.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.merryblue.femalefitness.ui.base.BaseViewModel;

public class RunFragmentViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isRun;

    public RunFragmentViewModel() {
        super();
        isRun = new MutableLiveData<>();
    }
}
