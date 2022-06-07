package com.vuthaihung.loseflat.ui.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.vuthaihung.loseflat.ui.base.BaseViewModel;

public class RunFragmentViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isRun;

    public RunFragmentViewModel() {
        super();
        isRun = new MutableLiveData<>();
    }
}
