package ch.epfl.sweng.hostme.ui.add;

import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;

import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;
import static ch.epfl.sweng.hostme.utils.Constants.UID;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.databinding.FragmentAddBinding;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;
import ch.epfl.sweng.hostme.utils.Connection;
import ch.epfl.sweng.hostme.utils.ListImage;
import ch.epfl.sweng.hostme.utils.Listing;

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
        final Button enterImages = binding.enterImages;
        addViewModel.key(enterImages);
        final Button addSubmit = binding.addSubmit;
        final FloatingActionButton addNew = binding.addNew;
        enterImages.setOnClickListener(v -> {
            addViewModel.formPath(Objects.requireNonNull(formFields.get("proprietor")), Objects.requireNonNull(formFields.get("name")),
                    Objects.requireNonNull(formFields.get("room")));
            if (ListImage.getPath() == null || !ListImage.getPath().equals(addViewModel.formPath().getValue())) {
                ListImage.init(addViewModel.formPath().getValue(), this, this.getContext());
            }
            ListImage.clear();
            ListImage.acceptImage();
            addViewModel.key(addSubmit);
        });
        addSubmit.setOnClickListener(v -> {
            generateApartment(root);
            clearForm();
            ListImage.clear();
            checkBin();
            formTransition(addForm, addButtons);
        });

        if (Connection.online(requireActivity())) {
            addNew.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.purple_100)));
        } else {
            addNew.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.grey)));
            addNew.setEnabled(false);
        }
        addNew.setOnClickListener(v -> formTransition(addForm, addButtons));

        ownerView = binding.ownerView;
        notOwner = binding.addFirst;
        myListings = new ArrayList<>();
        checkBin();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE && resultCode == Activity.RESULT_OK && data.getClipData() != null) {
            ListImage.onAcceptImage(resultCode, data.getClipData());
        }
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
            assert ref != null;
            ref.setOnFocusChangeListener((v, focused) -> addViewModel.validate(ref));
        }
    }

    private void clearForm() {
        for (String it : formFields.keySet()) Objects.requireNonNull(formFields.get(it)).setText("");
    }

    private void spinUp() {
        dropDowns.put("bath", binding.selectBath);
        dropDowns.put("kitchen", binding.selectKitchen);
        dropDowns.put("laundry", binding.selectLaundry);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this.getContext(),
                R.array.privacy_enum, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String menu : dropDowns.keySet()) {
            Objects.requireNonNull(dropDowns.get(menu)).setAdapter(arr);
            Objects.requireNonNull(dropDowns.get(menu)).setOnItemSelectedListener(addViewModel);
            Objects.requireNonNull(dropDowns.get(menu)).setSelection(1);
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

    private void generateApartment(View root) {
        JSONObject fields = new JSONObject();
        String[] privacy = getResources().getStringArray(R.array.privacy_enum);
        Button furn = root.findViewById(selectFurnished.getCheckedRadioButtonId());
        Button pet = root.findViewById(selectPets.getCheckedRadioButtonId());
        try {
            fields.put("name", Objects.requireNonNull(formFields.get("name")).getText().toString());
            fields.put("room", Objects.requireNonNull(formFields.get("room")).getText().toString());
            fields.put("address", Objects.requireNonNull(formFields.get("address")).getText().toString());
            fields.put("npa", Integer.valueOf(Objects.requireNonNull(formFields.get("npa")).getText().toString()));
            fields.put("city", Objects.requireNonNull(formFields.get("city")).getText().toString());
            fields.put("rent", Integer.valueOf(Objects.requireNonNull(formFields.get("rent")).getText().toString()));
            fields.put("beds", Integer.valueOf(Objects.requireNonNull(formFields.get("beds")).getText().toString()));
            fields.put("area", Integer.valueOf(Objects.requireNonNull(formFields.get("area")).getText().toString()));
            fields.put("furnished", furn.getText().toString().equals("yes"));
            fields.put("bath", privacy[Objects.requireNonNull(dropDowns.get("bath")).getSelectedItemPosition()]);
            fields.put("kitchen", privacy[Objects.requireNonNull(dropDowns.get("kitchen")).getSelectedItemPosition()]);
            fields.put("laundry", privacy[Objects.requireNonNull(dropDowns.get("laundry")).getSelectedItemPosition()]);
            fields.put("pets", pet.getText().toString().equals("yes"));
            fields.put("imagePath", addViewModel.formPath().getValue());
            fields.put("proprietor", Objects.requireNonNull(formFields.get("proprietor")).getText().toString());
            fields.put("uid", USR);
            fields.put("utilities", Integer.valueOf(Objects.requireNonNull(formFields.get("utilities")).getText().toString()));
            fields.put("deposit", Integer.valueOf(Objects.requireNonNull(formFields.get("deposit")).getText().toString()));
            fields.put("duration", Objects.requireNonNull(formFields.get("duration")).getText().toString());
        } catch (Exception ignored) {
        }

        ListImage.pushImages();
        Listing ret = new Listing(fields);
        DB.add(ret).addOnSuccessListener(doc -> Toast.makeText(this.getContext(), ADDED, Toast.LENGTH_SHORT).show());
    }

    private void checkBin() {
        myListings.clear();
        DB.whereEqualTo(UID, USR).get().addOnSuccessListener(q -> {
            if (q.isEmpty()) {
                ownerView.setVisibility(View.GONE);
                notOwner.setVisibility(View.VISIBLE);
            } else {
                for (DocumentSnapshot it: q.getDocuments()) {
                    myListings.add(it.toObject(Apartment.class));
                }
                notOwner.setVisibility(View.GONE);
                ownerView.setVisibility(View.VISIBLE);
                recycle();
            }
        });
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