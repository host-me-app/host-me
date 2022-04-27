package ch.epfl.sweng.hostme.ui.add;

import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

public class AddViewModel extends ViewModel implements AdapterView.OnItemSelectedListener {

    private final MutableLiveData<EditText> mField;
    private final MutableLiveData<String> empty;
    private Button submit;

    private Set<Integer> lock;

    public AddViewModel() {
        mField = new MutableLiveData<>();
        empty = new MutableLiveData<>();
        empty.setValue("You have no active listings");
        lock = new HashSet<>();
    }

    public void key(Button locked) {
        submit = locked;
    }
    public void selectField(EditText f) {
        mField.setValue(f);
    }
    public void validate() {
        EditText chk = mField.getValue();
        String input = chk.getText().toString();
        if (!input.isEmpty()) {
            if (chk.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                try {
                    Integer.valueOf(input);
                } catch (NumberFormatException no) {
                    lock.remove(chk.getId());
                    chk.setText(String.format("*%s - NaN", input));
                }
            }
            lock.add(chk.getId());
        } else {
            lock.remove(chk.getId());
        }
        if (!submit.isEnabled() && lock.size() == 12) {
            submit.setEnabled(true);
        } else if (submit.isEnabled() && lock.size() < 12) {
            submit.setEnabled(false);
        }
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

}