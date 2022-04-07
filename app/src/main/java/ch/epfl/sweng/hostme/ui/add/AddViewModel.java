package ch.epfl.sweng.hostme.ui.add;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {

    private final MutableLiveData<EditText> mField;
    private final MutableLiveData<String> empty;

    public AddViewModel() {
        mField = new MutableLiveData<>();
        empty = new MutableLiveData<>();
        empty.setValue("You have no active listings");
    }

    public void selectField(EditText f) {
        mField.setValue(f);
    }
    public LiveData<EditText> getField() {
        return mField;
    }
    public LiveData<String> notOwner() {
        return empty;
    }
}