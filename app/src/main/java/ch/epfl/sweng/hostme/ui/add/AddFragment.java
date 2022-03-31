package ch.epfl.sweng.hostme.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import ch.epfl.sweng.hostme.Listing;
import ch.epfl.sweng.hostme.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {
    private FragmentAddBinding binding;
    private Map<String, EditText> formFields;
    private Button addSubmit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final LinearLayout addForm = binding.addForm;
        formFields = new HashMap<>();
        formFields.put("name", binding.enterName);
        formFields.put("room", binding.enterRoom);
        formFields.put("address", binding.enterAddress);
        formFields.put("rent", binding.enterRent);
        formFields.put("beds", binding.enterBeds);
        formFields.put("area", binding.enterArea);
        formFields.put("furnished", binding.enterFurnished);
        formFields.put("bath", binding.enterBath);
        formFields.put("kitchen", binding.enterKitchen);
        formFields.put("laundry", binding.enterLaundry);
        formFields.put("pet", binding.enterPet);
        formFields.put("proprietor", binding.enterProprietor);
        formFields.put("utilities", binding.enterUtilities);
        formFields.put("deposit", binding.enterDeposit);
        formFields.put("duration", binding.enterDuration);

        final Button enterImages = binding.enterImages;
        enterImages.setOnClickListener(v -> {   // TODO: replace with image upload
            enterImages.setEnabled(false);
        });
        addSubmit = binding.addSubmit;
        addSubmit.setOnClickListener(v -> {
            Listing latest = generateApartment();
            try {
                System.out.println(latest.exportDoc().toString(2));
            } catch (JSONException e) { throw new RuntimeException(e); }
        });

        final FloatingActionButton addNew = binding.addNew;
        addNew.setOnClickListener(v -> {
            formTransition(addForm);
        });

        final TextView textView = binding.addWelcome;
        addViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void formTransition(LinearLayout form) {    // TODO: expand/ collapse from action button
        if (form.getVisibility() != View.VISIBLE) {
            form.setVisibility(View.VISIBLE);
        } else {
            form.setVisibility(View.GONE);
        }
        if (!addSubmit.isEnabled() && isFormFull()) {
            addSubmit.setEnabled(true);
        }
    }

    private boolean isFormFull() {  // TODO: replace with isDataValid
        boolean ret = true;
        for (String chk: formFields.keySet()) {
            ret &= !formFields.get(chk).getText().toString().isEmpty();
        }
        return ret;
    }

    private Listing generateApartment() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final CollectionReference apt = db.collection("apartments");
        JSONObject fields = new JSONObject();
        String addr[] = formFields.get("address").getText().toString().split("\n");
        try{
            fields.put("name", formFields.get("name").getText().toString());
            fields.put("room", formFields.get("room").getText().toString());
            fields.put("address", addr[0]);
            fields.put("npa", Integer.valueOf(addr[1].substring(0,4)));
            fields.put("city", addr[1].substring(5));
            fields.put("rent", Double.valueOf(formFields.get("rent").getText().toString()));
            fields.put("beds", Integer.valueOf(formFields.get("beds").getText().toString()));
            fields.put("area", Integer.valueOf(formFields.get("area").getText().toString()));
            fields.put("furnished", formFields.get("furnished").getText().toString().equals("yes"));
            fields.put("bath", formFields.get("bath").getText().toString());
            fields.put("kitchen", formFields.get("kitchen").getText().toString());
            fields.put("laundry", formFields.get("laundry").getText().toString());
            fields.put("pet", formFields.get("pet").getText().toString().equals("yes"));
            fields.put("proprietor", formFields.get("proprietor").getText().toString());
            fields.put("uid", auth.getUid());
            fields.put("utilities", Double.valueOf(formFields.get("utilities").getText().toString()));
            fields.put("deposit", Double.valueOf(formFields.get("deposit").getText().toString()));
            fields.put("duration", formFields.get("duration").getText().toString());
        } catch (JSONException e) { throw new RuntimeException(e); }

        Listing ret = new Listing(fields);

        apt.add(ret);

        return ret;
    }

}