package ch.epfl.sweng.hostme.ui.add;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    //private final MutableLiveData<EditText> mField;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("You have no active listings.");

        //mField = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}