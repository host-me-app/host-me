package ch.epfl.sweng.hostme.ui.add;

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
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.ListImage;

public class AddFragment extends Fragment {
    private static final String ADDED = "Listing created !";
    private final CollectionReference DB = Database.getCollection(Constants.APARTMENTS);
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
            addViewModel.formPath(Objects.requireNonNull(formFields.get(Constants.PROPRIETOR)),
                    Objects.requireNonNull(formFields.get(Constants.NAME)),
                    Objects.requireNonNull(formFields.get(Constants.ROOM)));
            if (ListImage.getPath() == null || !ListImage.getPath().equals(addViewModel.formPath().getValue())) {
                ListImage.init(addViewModel.formPath().getValue(), this, this.getContext());
            }
            ListImage.clear();
            ListImage.acceptImage();
            addViewModel.key(addSubmit);
        });
        addSubmit.setOnClickListener(v -> {
            myListings.add(generateApartment(root));
            ownerView.getAdapter().notifyItemInserted(myListings.size() - 1);
            clearForm();
            ListImage.clear();
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
        if (requestCode == Constants.REQ_IMAGE && resultCode == Activity.RESULT_OK && data.getClipData() != null) {
            ListImage.onAcceptImage(resultCode, data.getClipData());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void textValidation() {
        formFields.put(Constants.PROPRIETOR, binding.enterProprietor);
        formFields.put(Constants.NAME, binding.enterName);
        formFields.put(Constants.ROOM, binding.enterRoom);
        formFields.put(Constants.ADDRESS, binding.enterAddress);
        formFields.put(Constants.NPA, binding.enterNpa);
        formFields.put(Constants.CITY, binding.enterCity);
        formFields.put(Constants.RENT, binding.enterRent);
        formFields.put(Constants.UTILITIES, binding.enterUtilities);
        formFields.put(Constants.DEPOSIT, binding.enterDeposit);
        formFields.put(Constants.BEDS, binding.enterBeds);
        formFields.put(Constants.AREA, binding.enterArea);
        formFields.put(Constants.DURATION, binding.enterDuration);

        for (String it : formFields.keySet()) {
            EditText ref = formFields.get(it);
            assert ref != null;
            ref.setOnFocusChangeListener((v, focused) -> addViewModel.validate(ref));
        }
    }

    private void clearForm() {
        for (String it : formFields.keySet())
            Objects.requireNonNull(formFields.get(it)).setText("");
    }

    private void spinUp() {
        dropDowns.put(Constants.BATH, binding.selectBath);
        dropDowns.put(Constants.KITCHEN, binding.selectKitchen);
        dropDowns.put(Constants.LAUNDRY, binding.selectLaundry);

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

    private Apartment generateApartment(View root) {
        JSONObject fields = new JSONObject();
        String[] privacy = getResources().getStringArray(R.array.privacy_enum);
        Button furn = root.findViewById(selectFurnished.getCheckedRadioButtonId());
        Button pet = root.findViewById(selectPets.getCheckedRadioButtonId());
        try {
            fields.put(Constants.NAME, Objects.requireNonNull(formFields.get(Constants.NAME)).getText().toString());
            fields.put(Constants.ROOM, Objects.requireNonNull(formFields.get(Constants.ROOM)).getText().toString());
            fields.put(Constants.ADDRESS, Objects.requireNonNull(formFields.get(Constants.ADDRESS)).getText().toString());
            fields.put(Constants.NPA, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.NPA)).getText().toString()));
            fields.put(Constants.CITY, Objects.requireNonNull(formFields.get(Constants.CITY)).getText().toString());
            fields.put(Constants.RENT, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.RENT)).getText().toString()));
            fields.put(Constants.BEDS, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.BEDS)).getText().toString()));
            fields.put(Constants.AREA, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.AREA)).getText().toString()));
            fields.put(Constants.FURNISHED, furn.getText().toString().equals("yes"));
            fields.put(Constants.BATH, privacy[Objects.requireNonNull(dropDowns.get(Constants.BATH)).getSelectedItemPosition()]);
            fields.put(Constants.KITCHEN, privacy[Objects.requireNonNull(dropDowns.get(Constants.KITCHEN)).getSelectedItemPosition()]);
            fields.put(Constants.LAUNDRY, privacy[Objects.requireNonNull(dropDowns.get(Constants.LAUNDRY)).getSelectedItemPosition()]);
            fields.put(Constants.PETS, pet.getText().toString().equals("yes"));
            fields.put(Constants.IMAGE_PATH, addViewModel.formPath().getValue());
            fields.put(Constants.PROPRIETOR, Objects.requireNonNull(formFields.get(Constants.PROPRIETOR)).getText().toString());
            fields.put(Constants.UID, USR);
            fields.put(Constants.UTILITIES, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.UTILITIES)).getText().toString()));
            fields.put(Constants.DEPOSIT, Integer.valueOf(Objects.requireNonNull(formFields.get(Constants.DEPOSIT)).getText().toString()));
            fields.put(Constants.DURATION, Objects.requireNonNull(formFields.get(Constants.DURATION)).getText().toString());
        } catch (Exception ignored) {
        }

        ListImage.pushImages();
        Apartment ret = new Apartment(fields);

        DB.add(ret).addOnSuccessListener(doc -> {
            Map<String, Object> addition = new HashMap<>();
            addition.put("docId", doc.getId());
            doc.update(addition);
            Toast.makeText(this.getContext(), ADDED, Toast.LENGTH_SHORT).show();
        });
        return ret;
    }

    private void checkBin() {
        myListings.clear();
        DB.whereEqualTo(Constants.UID, USR).get().addOnSuccessListener(q -> {
            if (q.isEmpty()) {
                ownerView.setVisibility(View.GONE);
                notOwner.setVisibility(View.VISIBLE);
            } else {
                for (DocumentSnapshot it : q.getDocuments()) {
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