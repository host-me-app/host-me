package ch.epfl.sweng.hostme.ui.add;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel implements AdapterView.OnItemSelectedListener {

    private final MutableLiveData<EditText> mField;
    private final MutableLiveData<String> empty;

    public AddViewModel() {
        mField = new MutableLiveData<>();
        empty = new MutableLiveData<>();
        empty.setValue("You have no active listings");
    }

    public void selectField(EditText f) {
        if (mField.getValue() != null) {
            validate(mField.getValue());
        }
        mField.setValue(f);
    }
    public LiveData<EditText> getField() {
        return mField;
    }
    public LiveData<String> notOwner() {
        return empty;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(1);
    }
    private void validate(EditText chk) {
        // TODO: Check expected field content and pattern match/ check existence
    }
}