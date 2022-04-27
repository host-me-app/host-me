package ch.epfl.sweng.hostme.ui.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.Timestamp;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.FragmentAddBinding;
import ch.epfl.sweng.hostme.utils.ListImage;
import ch.epfl.sweng.hostme.utils.Listing;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

public class AddFragment extends Fragment {
    private static final String ADDED = "Listing created !";

    private FragmentAddBinding binding;
    private AddViewModel addViewModel;
    private Map<String, EditText> formFields;
    private Map<String, Spinner> dropDowns;
    private RadioGroup selectFurnished;
    private RadioGroup selectPets;
    private Button enterImages;
    private Button addSubmit;

    private final CollectionReference DB = Database.getCollection(APARTMENTS);
    private final String UID = Auth.getUid();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ScrollView addForm = binding.addForm;
        formFields = new HashMap<>();
        dropDowns = new HashMap<>();
        selectFurnished = binding.selectFurnished;
        selectPets = binding.selectPets;

        init();

        final LinearLayout addButtons = binding.addButtons;
        enterImages = binding.enterImages;
        addSubmit = binding.addSubmit;
        enterImages.setOnClickListener(v -> {
            addViewModel.formPath(formFields.get("proprietor"), formFields.get("name"),
                    formFields.get("room"));
            new ListImage(addViewModel.formPath().getValue(), this.getActivity(), this.getContext());
            addViewModel.key(addSubmit);
        });
        addSubmit.setOnClickListener(v -> {
            Listing latest = generateApartment(root);
        });

        final FloatingActionButton addNew = binding.addNew;
        addNew.setOnClickListener(v -> {
            formTransition(addForm, addButtons);
        });

        final TextView addFirst = binding.addFirst;
        if (!UID.isEmpty()) {
            addViewModel.notOwner().observe(getViewLifecycleOwner(), addFirst::setText);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        formFields.put("proprietor", binding.enterProprietor);
        formFields.put("name", binding.enterName);
        formFields.put("room", binding.enterRoom);
        formFields.put("address", binding.enterAddress);
        formFields.put("npa", binding.enterNpa);
        formFields.put("city", binding.enterCity);
        formFields.put("rent", binding.enterRent);
        formFields.put("utilities", binding.enterUtilities);
        formFields.put("deposit", binding.enterDeposit);
        formFields.put("beds", binding.enterBeds);
        formFields.put("area", binding.enterArea);
        formFields.put("duration", binding.enterDuration);

        for (String it: formFields.keySet()) {
            EditText ref = formFields.get(it);
            ref.setOnClickListener(v -> {
                addViewModel.selectField(ref);
            });
            ref.addTextChangedListener(fieldWatcher);
        }

        dropDowns.put("bath", binding.selectBath);
        dropDowns.put("kitchen", binding.selectKitchen);
        dropDowns.put("laundry", binding.selectLaundry);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this.getContext(),
                R.array.privacy_enum , android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String menu : dropDowns.keySet()) {
            dropDowns.get(menu).setAdapter(arr);
            dropDowns.get(menu).setOnItemSelectedListener(addViewModel);
        }
    }

    private void formTransition(ScrollView form, LinearLayout buttons) {    // TODO: expand/ collapse from action button
        if (form.getVisibility() != View.VISIBLE) {
            form.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
        } else {
            form.setVisibility(View.GONE);
            buttons.setVisibility(View.GONE);
        }
    }

    private Listing generateApartment(View root) {
        JSONObject fields = new JSONObject();
        String[] priv = getResources().getStringArray(R.array.privacy_enum);
        Button furn = root.findViewById(selectFurnished.getCheckedRadioButtonId());
        Button pet = root.findViewById(selectPets.getCheckedRadioButtonId());
        try{
            fields.put("name", formFields.get("name").getText().toString());
            fields.put("room", formFields.get("room").getText().toString());
            fields.put("address", formFields.get("address").getText().toString());
            fields.put("npa", Integer.valueOf(formFields.get("npa").getText().toString()));
            fields.put("city", formFields.get("city").getText().toString());
            fields.put("rent", Integer.valueOf(formFields.get("rent").getText().toString()));
            fields.put("beds", Integer.valueOf(formFields.get("beds").getText().toString()));
            fields.put("area", Integer.valueOf(formFields.get("area").getText().toString()));
            fields.put("furnished", furn.getText().toString().equals("yes"));
            fields.put("bath", priv[dropDowns.get("bath").getSelectedItemPosition()]);
            fields.put("kitchen", priv[dropDowns.get("kitchen").getSelectedItemPosition()]);
            fields.put("laundry", priv[dropDowns.get("laundry").getSelectedItemPosition()]);
            fields.put("pets", pet.getText().toString().equals("yes"));
            fields.put("imageDir", addViewModel.formPath().getValue());
            fields.put("proprietor", formFields.get("proprietor").getText().toString());
            fields.put("uid", UID);
            fields.put("utilities", Integer.valueOf(formFields.get("utilities").getText().toString()));
            fields.put("deposit", Integer.valueOf(formFields.get("deposit").getText().toString()));
            fields.put("duration", formFields.get("duration").getText().toString());
        } catch (JSONException e) { throw new RuntimeException(e); }

        Listing ret = new Listing(fields);

        DB.add(ret).addOnSuccessListener(doc -> {
            Toast.makeText(this.getContext(), ADDED, Toast.LENGTH_SHORT).show();
        });

        return ret;
    }

    private TextWatcher fieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            addViewModel.validate();
        }
    };

}