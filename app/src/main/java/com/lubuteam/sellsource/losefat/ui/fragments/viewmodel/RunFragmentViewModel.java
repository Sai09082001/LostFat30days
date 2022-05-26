package com.lubuteam.sellsource.losefat.ui.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.lubuteam.sellsource.losefat.ui.base.BaseViewModel;

public class RunFragmentViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isRun;

    public RunFragmentViewModel() {
        super();
        isRun = new MutableLiveData<>();
    }
}
