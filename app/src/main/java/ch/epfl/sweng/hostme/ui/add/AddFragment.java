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
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;
import ch.epfl.sweng.hostme.utils.Connection;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.ListImage;

public class AddFragment extends Fragment {
    private static final String ADDED = "Listing created !";
    private static final String DOC_ID = "docId";
    private static final String YES = "yes";
    private final CollectionReference DB = Database.getCollection(Constants.APARTMENTS);
    private final String USR = Auth.getUid();
    private View root;
    private AddViewModel addViewModel;
    private Map<String, EditText> formFields;
    private Map<String, Spinner> dropDowns;
    private RadioGroup selectFurnished;
    private RadioGroup selectPets;
    private RecyclerView ownerView;
    private List<Apartment> myListings;
    private TextView notOwner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_add, container, false);

        this.addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        this.formFields = new HashMap<>();
        this.dropDowns = new HashMap<>();
        this.selectFurnished = this.root.findViewById(R.id.select_furnished);
        this.selectPets = this.root.findViewById(R.id.select_pets);
        this.ownerView = this.root.findViewById(R.id.owner_view);
        this.notOwner = this.root.findViewById(R.id.add_first);
        this.myListings = new ArrayList<>();

        LinearLayout addButtons = this.root.findViewById(R.id.add_buttons);
        ScrollView addForm = this.root.findViewById(R.id.add_form);
        FloatingActionButton addNew = this.root.findViewById(R.id.add_new);
        Button addSubmit = this.root.findViewById(R.id.add_submit);
        Button enterImages = this.root.findViewById(R.id.enter_images);

        this.addViewModel.key(enterImages);
        this.setButtonListener(addSubmit, enterImages, addForm, addNew, addButtons);
        this.textValidation();
        this.spinUp();
        this.checkBin();

        return root;
    }

    private void setButtonListener(Button addSubmit, Button enterImages, ScrollView addForm, FloatingActionButton addNew, LinearLayout addButtons) {
        enterImages.setOnClickListener(v -> {
            this.addViewModel.formPath(Objects.requireNonNull(this.formFields.get(Constants.PROPRIETOR)),
                    Objects.requireNonNull(this.formFields.get(Constants.NAME)),
                    Objects.requireNonNull(this.formFields.get(Constants.ROOM)));
            if (ListImage.getPath() == null || !ListImage.getPath().equals(this.addViewModel.formPath().getValue())) {
                ListImage.init(this.addViewModel.formPath().getValue(), this, this.getContext());
            }
            ListImage.clear();
            ListImage.acceptImage();
            this.addViewModel.key(addSubmit);
        });

        addSubmit.setOnClickListener(v -> {
            this.myListings.add(generateApartment(root));
            Objects.requireNonNull(this.ownerView.getAdapter()).notifyItemInserted(myListings.size() - 1);
            clearForm();
            ListImage.clear();
            formTransition(addForm, addButtons);
        });

        if (Connection.online(requireActivity())) {
            addNew.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_100)));
        } else {
            addNew.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            addNew.setEnabled(false);
        }
        addNew.setOnClickListener(v -> formTransition(addForm, addButtons));
    }

    private void textValidation() {
        this.formFields.put(Constants.PROPRIETOR, this.root.findViewById(R.id.enter_proprietor));
        this.formFields.put(Constants.NAME, this.root.findViewById(R.id.enter_name));
        this.formFields.put(Constants.ROOM, this.root.findViewById(R.id.enter_room));
        this.formFields.put(Constants.ADDRESS, this.root.findViewById(R.id.enter_address));
        this.formFields.put(Constants.NPA, this.root.findViewById(R.id.enter_npa));
        this.formFields.put(Constants.CITY, this.root.findViewById(R.id.enter_city));
        this.formFields.put(Constants.RENT, this.root.findViewById(R.id.enter_rent));
        this.formFields.put(Constants.UTILITIES, this.root.findViewById(R.id.enter_utilities));
        this.formFields.put(Constants.DEPOSIT, this.root.findViewById(R.id.enter_deposit));
        this.formFields.put(Constants.BEDS, this.root.findViewById(R.id.enter_beds));
        this.formFields.put(Constants.AREA, this.root.findViewById(R.id.enter_area));
        this.formFields.put(Constants.DURATION, this.root.findViewById(R.id.enter_duration));

        for (String it : this.formFields.keySet()) {
            EditText ref = this.formFields.get(it);
            assert ref != null;
            ref.setOnFocusChangeListener((v, focused) -> this.addViewModel.validate(ref));
        }
    }

    private void clearForm() {
        for (String it : this.formFields.keySet())
            Objects.requireNonNull(this.formFields.get(it)).setText("");
    }

    private void spinUp() {
        this.dropDowns.put(Constants.BATH, this.root.findViewById(R.id.select_bath));
        this.dropDowns.put(Constants.KITCHEN, this.root.findViewById(R.id.select_kitchen));
        this.dropDowns.put(Constants.LAUNDRY, this.root.findViewById(R.id.select_laundry));

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this.getContext(),
                R.array.privacy_enum, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String menu : this.dropDowns.keySet()) {
            Objects.requireNonNull(this.dropDowns.get(menu)).setAdapter(arr);
            Objects.requireNonNull(this.dropDowns.get(menu)).setOnItemSelectedListener(this.addViewModel);
            Objects.requireNonNull(this.dropDowns.get(menu)).setSelection(1);
        }
    }

    private void formTransition(ScrollView form, LinearLayout buttons) {
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
        Button furn = root.findViewById(this.selectFurnished.getCheckedRadioButtonId());
        Button pet = root.findViewById(this.selectPets.getCheckedRadioButtonId());
        try {
            fields.put(Constants.NAME, Objects.requireNonNull(this.formFields.get(Constants.NAME)).getText().toString());
            fields.put(Constants.ROOM, Objects.requireNonNull(this.formFields.get(Constants.ROOM)).getText().toString());
            fields.put(Constants.ADDRESS, Objects.requireNonNull(this.formFields.get(Constants.ADDRESS)).getText().toString());
            fields.put(Constants.NPA, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.NPA)).getText().toString()));
            fields.put(Constants.CITY, Objects.requireNonNull(this.formFields.get(Constants.CITY)).getText().toString());
            fields.put(Constants.RENT, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.RENT)).getText().toString()));
            fields.put(Constants.BEDS, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.BEDS)).getText().toString()));
            fields.put(Constants.AREA, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.AREA)).getText().toString()));
            fields.put(Constants.FURNISHED, furn.getText().toString().equals(YES));
            fields.put(Constants.BATH, privacy[Objects.requireNonNull(this.dropDowns.get(Constants.BATH)).getSelectedItemPosition()]);
            fields.put(Constants.KITCHEN, privacy[Objects.requireNonNull(this.dropDowns.get(Constants.KITCHEN)).getSelectedItemPosition()]);
            fields.put(Constants.LAUNDRY, privacy[Objects.requireNonNull(this.dropDowns.get(Constants.LAUNDRY)).getSelectedItemPosition()]);
            fields.put(Constants.PETS, pet.getText().toString().equals(YES));
            fields.put(Constants.IMAGE_PATH, this.addViewModel.formPath().getValue());
            fields.put(Constants.PROPRIETOR, Objects.requireNonNull(this.formFields.get(Constants.PROPRIETOR)).getText().toString());
            fields.put(Constants.UID, USR);
            fields.put(Constants.UTILITIES, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.UTILITIES)).getText().toString()));
            fields.put(Constants.DEPOSIT, Integer.valueOf(Objects.requireNonNull(this.formFields.get(Constants.DEPOSIT)).getText().toString()));
            fields.put(Constants.DURATION, Objects.requireNonNull(this.formFields.get(Constants.DURATION)).getText().toString());
        } catch (Exception ignored) {
        }

        ListImage.pushImages();
        Apartment ret = new Apartment(fields);

        DB.add(ret).addOnSuccessListener(doc -> {
            Map<String, Object> addition = new HashMap<>();
            addition.put(DOC_ID, doc.getId());
            doc.update(addition);
            Toast.makeText(this.getContext(), ADDED, Toast.LENGTH_SHORT).show();
        });
        return ret;
    }

    private void checkBin() {
        this.myListings.clear();
        DB.whereEqualTo(Constants.UID, USR).get().addOnSuccessListener(q -> {
            if (q.isEmpty()) {
                ownerView.setVisibility(View.GONE);
                notOwner.setVisibility(View.VISIBLE);
            } else {
                for (DocumentSnapshot it : q.getDocuments()) {
                    this.myListings.add(it.toObject(Apartment.class));
                }
                this.notOwner.setVisibility(View.GONE);
                this.ownerView.setVisibility(View.VISIBLE);
                recycle();
            }
        });
    }

    private void recycle() {
        ApartmentAdapter recycler = new ApartmentAdapter(myListings);
        recycler.setFavFragment();
        RecyclerView.LayoutManager lin = new LinearLayoutManager(this.getContext());
        this.ownerView.setLayoutManager(lin);
        this.ownerView.setAdapter(recycler);
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
    }
}