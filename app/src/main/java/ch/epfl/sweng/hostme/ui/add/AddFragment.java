package ch.epfl.sweng.hostme.ui.add;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.FragmentAddBinding;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;
import ch.epfl.sweng.hostme.utils.ListImage;
import ch.epfl.sweng.hostme.utils.Listing;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

public class AddFragment extends Fragment {
    private static final String ADDED = "Listing created !";
    private final CollectionReference DB = Database.getCollection(APARTMENTS);
    private final String USR = Auth.getUid();
    private FragmentAddBinding binding;
    private AddViewModel addViewModel;
    private Map<String, EditText> formFields;
    private Map<String, Spinner> dropDowns;
    private RadioGroup selectFurnished;
    private RadioGroup selectPets;
    private Button enterImages;
    private Button addSubmit;
    private RecyclerView ownerView;
    private List<Apartment> myListings;
    private TextView notOwner;

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

        textValidation();
        spinUp();

        final LinearLayout addButtons = binding.addButtons;
        enterImages = binding.enterImages;
        addViewModel.key(enterImages);
        addSubmit = binding.addSubmit;
        enterImages.setOnClickListener(v -> {
            addViewModel.formPath(formFields.get("proprietor"), formFields.get("name"),
                    formFields.get("room"));
            if (ListImage.getPath() == null || !ListImage.getPath().equals(addViewModel.formPath().getValue())) {
                ListImage.init(addViewModel.formPath().getValue(), this.getActivity(), this.getContext());
            }
            ListImage.acceptImage();
            addViewModel.key(addSubmit);
        });
        addSubmit.setOnClickListener(v -> {
            Listing latest = generateApartment(root);
        });

        final FloatingActionButton addNew = binding.addNew;
        addNew.setOnClickListener(v -> {
            formTransition(addForm, addButtons);
        });

        ownerView = binding.ownerView;
        notOwner = binding.addFirst;
        myListings = checkBin();
        /* if (!myListings.isEmpty()) {
            System.out.println("Not displaying ?");
            recycle();
        } */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void textValidation() {
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

        for (String it : formFields.keySet()) {
            EditText ref = formFields.get(it);
            ref.setOnFocusChangeListener((v, focused) -> {
                addViewModel.validate(ref);
            });
        }
    }

    private void spinUp() {
        dropDowns.put("bath", binding.selectBath);
        dropDowns.put("kitchen", binding.selectKitchen);
        dropDowns.put("laundry", binding.selectLaundry);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this.getContext(),
                R.array.privacy_enum, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String menu : dropDowns.keySet()) {
            dropDowns.get(menu).setAdapter(arr);
            dropDowns.get(menu).setOnItemSelectedListener(addViewModel);
            dropDowns.get(menu).setSelection(1);
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
        try {
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
            fields.put("imagePath", addViewModel.formPath().getValue());
            fields.put("proprietor", formFields.get("proprietor").getText().toString());
            fields.put("uid", USR);
            fields.put("utilities", Integer.valueOf(formFields.get("utilities").getText().toString()));
            fields.put("deposit", Integer.valueOf(formFields.get("deposit").getText().toString()));
            fields.put("duration", formFields.get("duration").getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Listing ret = new Listing(fields);

        DB.add(ret).addOnSuccessListener(doc -> {
            Toast.makeText(this.getContext(), ADDED, Toast.LENGTH_SHORT).show();
        });

        return ret;
    }

    private List<Apartment> checkBin() {
        List<Apartment> ret = new ArrayList<>();
        DB.whereEqualTo(UID, USR).get().addOnSuccessListener(q -> {
            if (q.isEmpty()) {
                ownerView.setVisibility(View.GONE);
                notOwner.setVisibility(View.VISIBLE);
            } else {
                for (DocumentSnapshot it: q.getDocuments()) {
                    ret.add(it.toObject(Apartment.class));
                }
                notOwner.setVisibility(View.GONE);
                ownerView.setVisibility(View.VISIBLE);
                recycle();
            }
        });
        return ret;
    }

    private void recycle() {
        ApartmentAdapter recycler = new ApartmentAdapter(myListings, this.getContext());
        recycler.setFavFragment();
        RecyclerView.LayoutManager lin = new LinearLayoutManager(this.getContext());
        ownerView.setHasFixedSize(true);
        ownerView.setLayoutManager(lin);
        ownerView.setAdapter(recycler);
    }
}