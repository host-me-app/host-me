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

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

public class AddViewModel extends ViewModel implements AdapterView.OnItemSelectedListener {

    private final MutableLiveData<EditText> mField;
    private final MutableLiveData<Button> action;
    private final MutableLiveData<String> location;
    private final MutableLiveData<String> empty;

    private Set<Integer> lock;

    public AddViewModel() {
        mField = new MutableLiveData<>();
        action = new MutableLiveData<>();
        location = new MutableLiveData<>();
        empty = new MutableLiveData<>();
        empty.setValue("You have no active listings");
        lock = new HashSet<>();
    }

    public void key(Button locked) {
        action.setValue(locked);
        turn();
    }
    private void turn() {
        if (!action.getValue().isEnabled() && lock.size() == 12) {
            action.getValue().setEnabled(true);
        } else if (action.getValue().isEnabled() && lock.size() < 12) {
            action.getValue().setEnabled(false);
        }
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
        turn();
    }
    public void formPath(EditText p, EditText b, EditText r) {
        location.setValue(String.format("%s/%s_%s_%s", APARTMENTS, p.getText().toString().toLowerCase(),
                b.getText().toString().toLowerCase(), r.getText().toString().toLowerCase()));
    }
    public LiveData<String> formPath() {
        return location;
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