package com.lubuteam.sellsource.losefat.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.lubuteam.sellsource.losefat.ui.base.BaseViewModel;

public class EditWorkoutViewModel extends BaseViewModel {
    public MutableLiveData<Integer> seconds;
    public MutableLiveData<Integer> counts;

    public EditWorkoutViewModel() {
        super();
        seconds = new MutableLiveData<>(0);
        counts = new MutableLiveData<>(0);
    }
}
