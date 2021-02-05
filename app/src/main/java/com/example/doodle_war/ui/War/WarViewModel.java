package com.example.doodle_war.ui.War;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is War fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}