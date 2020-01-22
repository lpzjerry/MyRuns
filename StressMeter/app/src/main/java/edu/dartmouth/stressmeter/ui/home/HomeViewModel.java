package edu.dartmouth.stressmeter.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Touch The image that best captures how stressed you feel right now");
    }

    public LiveData<String> getText() {
        return mText;
    }
}