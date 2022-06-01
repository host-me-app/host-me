package ch.epfl.sweng.hostme.ui.add;

import static ch.epfl.sweng.hostme.utils.Constants.ADDRESS;
import static ch.epfl.sweng.hostme.utils.Constants.APARTMENTS;
import static ch.epfl.sweng.hostme.utils.Constants.AREA;
import static ch.epfl.sweng.hostme.utils.Constants.BATH;
import static ch.epfl.sweng.hostme.utils.Constants.BEDS;
import static ch.epfl.sweng.hostme.utils.Constants.CITY;
import static ch.epfl.sweng.hostme.utils.Constants.DEPOSIT;
import static ch.epfl.sweng.hostme.utils.Constants.DURATION;
import static ch.epfl.sweng.hostme.utils.Constants.FURNISHED;
import static ch.epfl.sweng.hostme.utils.Constants.IMAGE_PATH;
import static ch.epfl.sweng.hostme.utils.Constants.KITCHEN;
import static ch.epfl.sweng.hostme.utils.Constants.LAUNDRY;
import static ch.epfl.sweng.hostme.utils.Constants.NAME;
import static ch.epfl.sweng.hostme.utils.Constants.NPA;
import static ch.epfl.sweng.hostme.utils.Constants.PETS;
import static ch.epfl.sweng.hostme.utils.Constants.PROPRIETOR;
import static ch.epfl.sweng.hostme.utils.Constants.RENT;
import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;
import static ch.epfl.sweng.hostme.utils.Constants.ROOM;
import static ch.epfl.sweng.hostme.utils.Constants.UID;
import static ch.epfl.sweng.hostme.utils.Constants.UTILITIES;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.ui.search.ApartmentAdapter;
import ch.epfl.sweng.hostme.utils.Apartment;
import ch.epfl.sweng.hostme.utils.Connection;
import ch.epfl.sweng.hostme.utils.ListImage;

public class AddFragment extends Fragment {
    private static final String YES = "yes";
    private final CollectionReference DB = Database.getCollection(APARTMENTS);
    private final String USR = Auth.getUid();
    private View root;
    private Set<Integer> lock;
    private Button enterImages;
    private Button addSubmit;
    private Map<String, EditText> formFields;
    private Map<String, Spinner> dropDowns;
    private RadioGroup selectFurnished;
    private RadioGroup selectPets;
    private RecyclerView ownerView;
    private List<Apartment> myListings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_add, container, false);

        this.formFields = new HashMap<>();
        this.dropDowns = new HashMap<>();
        this.lock = new HashSet<>();

        this.selectFurnished = this.root.findViewById(R.id.select_furnished);
        this.selectPets = this.root.findViewById(R.id.select_pets);
        this.ownerView = this.root.findViewById(R.id.owner_view);
        this.myListings = new ArrayList<>();

        LinearLayout addButtons = this.root.findViewById(R.id.add_buttons);
        ScrollView addForm = this.root.findViewById(R.id.add_form);
        ImageButton addNew = this.root.findViewById(R.id.add_new);
        this.enterImages = this.root.findViewById(R.id.enter_images);
        this.addSubmit = this.root.findViewById(R.id.add_submit);

        ListImage.init(this);

        this.setButtonListener(addForm, addNew, addButtons);
        this.textValidation();
        this.spinUp();
        this.checkBin();

        return root;
    }

    private void setButtonListener(ScrollView addForm, ImageButton addNew, LinearLayout addButtons) {
        enterImages.setOnClickListener(v -> {
            ListImage.clear();
            ListImage.acceptImage();
        });

        addSubmit.setOnClickListener(v -> {
            generateApartment();
            clearForm();
            ListImage.clear();
            this.enterImages.setEnabled(false);
            this.addSubmit.setEnabled(false);
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
        this.formFields.put(PROPRIETOR, this.root.findViewById(R.id.enter_proprietor));
        this.formFields.put(NAME, this.root.findViewById(R.id.enter_name));
        this.formFields.put(ROOM, this.root.findViewById(R.id.enter_room));
        this.formFields.put(ADDRESS, this.root.findViewById(R.id.enter_address));
        this.formFields.put(NPA, this.root.findViewById(R.id.enter_npa));
        this.formFields.put(CITY, this.root.findViewById(R.id.enter_city));
        this.formFields.put(RENT, this.root.findViewById(R.id.enter_rent));
        this.formFields.put(UTILITIES, this.root.findViewById(R.id.enter_utilities));
        this.formFields.put(DEPOSIT, this.root.findViewById(R.id.enter_deposit));
        this.formFields.put(BEDS, this.root.findViewById(R.id.enter_beds));
        this.formFields.put(AREA, this.root.findViewById(R.id.enter_area));
        this.formFields.put(DURATION, this.root.findViewById(R.id.enter_duration));

        for (String it : this.formFields.keySet()) {
            EditText ref = this.formFields.get(it);
            assert ref != null;
            ref.setOnFocusChangeListener((v, focused) -> this.validate(ref));
        }
    }

    private void clearForm() {
        for (String it : this.formFields.keySet())
            Objects.requireNonNull(this.formFields.get(it)).setText("");
    }

    private void spinUp() {
        this.dropDowns.put(BATH, this.root.findViewById(R.id.select_bath));
        this.dropDowns.put(KITCHEN, this.root.findViewById(R.id.select_kitchen));
        this.dropDowns.put(LAUNDRY, this.root.findViewById(R.id.select_laundry));

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this.getContext(),
                R.array.privacy_enum, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String menu : this.dropDowns.keySet()) {
            Objects.requireNonNull(this.dropDowns.get(menu)).setAdapter(arr);
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

    private JSONObject fillFields(String[] privacy, Button furn, Button pet, String path) {
        JSONObject fields = new JSONObject();

        try {
            fields.put(NAME, Objects.requireNonNull(this.formFields.get(NAME)).getText().toString());
            fields.put(ROOM, Objects.requireNonNull(this.formFields.get(ROOM)).getText().toString());
            fields.put(ADDRESS, Objects.requireNonNull(this.formFields.get(ADDRESS)).getText().toString());
            fields.put(NPA, Integer.valueOf(Objects.requireNonNull(this.formFields.get(NPA)).getText().toString()));
            fields.put(CITY, Objects.requireNonNull(this.formFields.get(CITY)).getText().toString());
            fields.put(RENT, Integer.valueOf(Objects.requireNonNull(this.formFields.get(RENT)).getText().toString()));
            fields.put(BEDS, Integer.valueOf(Objects.requireNonNull(this.formFields.get(BEDS)).getText().toString()));
            fields.put(AREA, Integer.valueOf(Objects.requireNonNull(this.formFields.get(AREA)).getText().toString()));
            fields.put(FURNISHED, furn.getText().toString().equals(YES));
            fields.put(BATH, privacy[Objects.requireNonNull(this.dropDowns.get(BATH)).getSelectedItemPosition()]);
            fields.put(KITCHEN, privacy[Objects.requireNonNull(this.dropDowns.get(KITCHEN)).getSelectedItemPosition()]);
            fields.put(LAUNDRY, privacy[Objects.requireNonNull(this.dropDowns.get(LAUNDRY)).getSelectedItemPosition()]);
            fields.put(PETS, pet.getText().toString().equals(YES));
            fields.put(IMAGE_PATH, path);
            fields.put(PROPRIETOR, Objects.requireNonNull(this.formFields.get(PROPRIETOR)).getText().toString());
            fields.put(UID, USR);
            fields.put(UTILITIES, Integer.valueOf(Objects.requireNonNull(this.formFields.get(UTILITIES)).getText().toString()));
            fields.put(DEPOSIT, Integer.valueOf(Objects.requireNonNull(this.formFields.get(DEPOSIT)).getText().toString()));
            fields.put(DURATION, Objects.requireNonNull(this.formFields.get(DURATION)).getText().toString());
        } catch (Exception ignored) {
        }

        return fields;
    }

    private void generateApartment() {
        String[] privacy = getResources().getStringArray(R.array.privacy_enum);
        Button furn = this.root.findViewById(this.selectFurnished.getCheckedRadioButtonId());
        Button pet = this.root.findViewById(this.selectPets.getCheckedRadioButtonId());
        String path = this.formPath(Objects.requireNonNull(this.formFields.get(PROPRIETOR)),
                Objects.requireNonNull(this.formFields.get(NAME)),
                Objects.requireNonNull(this.formFields.get(ROOM)));

        JSONObject fields = this.fillFields(privacy, furn, pet, path);

        Apartment ret = new Apartment(fields);

        ListImage.pushImages(path, ret, this.myListings, this.ownerView.getAdapter());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void checkBin() {
        this.myListings.clear();
        this.recycle();
        DB.whereEqualTo(UID, USR).get().addOnSuccessListener(q -> {
            for (DocumentSnapshot it : q.getDocuments()) {
                this.myListings.add(it.toObject(Apartment.class));
                Objects.requireNonNull(this.ownerView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    private void recycle() {
        ApartmentAdapter recycler = new ApartmentAdapter(myListings);
        RecyclerView.LayoutManager lin = new LinearLayoutManager(this.getContext());
        this.ownerView.setLayoutManager(lin);
        this.ownerView.setHasFixedSize(true);
        this.ownerView.setItemViewCacheSize(20);
        this.ownerView.setDrawingCacheEnabled(true);
        this.ownerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        this.ownerView.setAdapter(recycler);
    }

    private void turn() {
        if (!enterImages.isEnabled() && lock.size() == 12) {
            this.enterImages.setEnabled(true);
        } else if (lock.size() < 12) {
            this.enterImages.setEnabled(false);
            this.addSubmit.setEnabled(false);
        }
        if (!this.addSubmit.isEnabled() && enterImages.isEnabled() && ListImage.areImagesSelected()) {
            this.addSubmit.setEnabled(true);
        }
    }

    private String formPath(EditText p, EditText b, EditText r) {
        return String.format("%s/%s_%s_%s", APARTMENTS, p.getText().toString().toLowerCase(),
                b.getText().toString().toLowerCase(), r.getText().toString().toLowerCase());
    }

    private void validate(EditText editText) {
        if (this.lock.size() == 11 || this.lock.size() == 12) {
            editText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (String.valueOf(s).isEmpty()) {
                        lock.remove(editText.getId());
                    } else {
                        lock.add(editText.getId());
                    }
                    turn();
                }
            });
        } else {
            String input = editText.getText().toString();
            if (!input.isEmpty()) {
                this.lock.add(editText.getId());
            } else {
                this.lock.remove(editText.getId());
            }
        }
        turn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE && resultCode == Activity.RESULT_OK && data.getClipData() != null) {
            ListImage.onAcceptImage(resultCode, data.getClipData());
            this.turn();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

