package com.example.doodle_war.bottomnavigation.draw;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DrawViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DrawViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is War fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}