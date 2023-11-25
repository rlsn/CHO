package com.example.cho.ui.meal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MealViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("下次一定");
    }

    public LiveData<String> getText() {
        return mText;
    }
}