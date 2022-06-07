package com.vuthaihung.loseflat.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.vuthaihung.loseflat.ui.base.BaseViewModel;

public class EditWorkoutViewModel extends BaseViewModel {
    public MutableLiveData<Integer> seconds;
    public MutableLiveData<Integer> counts;

    public EditWorkoutViewModel() {
        super();
        seconds = new MutableLiveData<>(0);
        counts = new MutableLiveData<>(0);
    }
}
