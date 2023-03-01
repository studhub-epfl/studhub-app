package com.studhub.app.ui.Browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowseViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BrowseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Will grow into a meaningful fragment someday.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}